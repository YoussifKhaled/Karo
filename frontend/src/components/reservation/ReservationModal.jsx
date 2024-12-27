import './ReservationModal.css';
import { useState,useEffect } from 'react';
    
const fetchReservations = async (lotId, spotId, token) => {
    const response = await fetch(
        `http://localhost:8080/reservations/by-lot-and-spot/future?lotId=${lotId}&spotId=${spotId}`, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${token}`
        }
    });
    const data = await response.json();
    console.log(data);
    return Array.isArray(data) ? data : [];
};
    
function Reservation ({ lot, spot, onClose }) {

    const [reservations, setReservations] = useState([]);
    const token = localStorage.getItem('token');

    useEffect(() => {
        const getReservations = async () => {
            const data = await fetchReservations(lot, spot, token);
            // loop through data and divide each reservation into hours
            // for example: reservation from 10:00 to 12:00 will be divided into 10:00-11:00 and 11:00-12:00
            // then set the reservations
            let resTmp = [];
            for (let i = 0; i < data.length; i++) {
                const start = new Date(data[i].start);
                const end = new Date(data[i].end);
                for (let j = start.getHours(); j < end.getHours(); j++) {
                    resTmp.push({
                        start: start.getFullYear() + '-' + (start.getMonth() + 1) + '-' + start.getDate() + 'T' + (j < 10 ? '0' : '') + j + ':00:00',
                        end: start.getFullYear() + '-' + (start.getMonth() + 1) + '-' + start.getDate() + 'T' + (j + 1 < 10 ? '0' : '') + (j + 1) + ':00:00'
                    });
                }
                
            }
            setReservations(resTmp);
        };
        getReservations();
    }, [lot, spot, token]);

    const now = new Date();
    const daysOfWeek = ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'];
    const [selectedHours, setSelectedHours] = useState([]);

    const getHours = (from) => {
        const hours = [];
        for (let i = from; i < 24; i++) {
            hours.push((i<10?'0':'') + i + ':00 - ' + (i+1<10?'0':'') + (i+1) + ':00');
        }
        return hours;
    }

    const getNextDays = () => {
        const days = [];
        for (let i = 0; i < 7; i++) {
            const date = new Date(now);
            const hours = getHours(i===0 ? now.getHours() : 0);
            date.setDate(now.getDate() + i);
            days.push({
                day: daysOfWeek[date.getDay()],
                date: date.getDate(),
                month: date.getMonth() + 1,
                fullDate: date.getFullYear() + '-' + (date.getMonth() + 1) + '-' + date.getDate(),
                hours: hours
            });
        }
        return days;
    }

    const days = getNextDays();
    const [selectedDay, setSelectedDay] = useState(days[0]);

    const handleClose = (close, e) => {
        if (close) {
            onClose();
        }else{
            e.stopPropagation();
        }
    }

    const UpdateSelectedDay = (day) => () => {
        setSelectedDay(day);
    }

    const isReserved = (day, hour) => {
        if (reservations.length === 0) {
            return false;
        }
        const startHour = hour.split(' ')[0]+':00';
        const lookFor = day.fullDate + 'T' + startHour;
        return reservations.some(res => res.start === lookFor);
    }

    const selectHours = (day, hour) => () => {
        if (isReserved(day, hour)) {
            return;
        }

        const startHour = hour.split(' ')[0]+':00';
        const endHour = hour.split(' ')[2]+':00';
        const lookForStart = day.fullDate + 'T' + startHour;
        const lookForEnd = day.fullDate + 'T' + endHour;

        if (selectedHours.some(hour => hour.start === lookForStart && hour.end === lookForEnd)) {
            setSelectedHours(selectedHours.filter(hour => hour.start !== lookForStart && hour.end !== lookForEnd));
            return;
        }

        setSelectedHours([...selectedHours, {start: lookForStart, end: lookForEnd}]);
    }

    const groupSelectedHours = (hours) => {

        hours.sort((a, b) => new Date(a.start) - new Date(b.start));

        const groupedHours = [];
        let start = hours[0].start;
        let end = hours[0].end;

        for (let i = 1; i < hours.length; i++) {
            const current = new Date(hours[i].start);
            const previous = new Date(hours[i - 1].start);

            if (current.getTime() - previous.getTime() === 3600000) {
                end = hours[i].end;
            } else {
                groupedHours.push({
                    start: start,
                    end: end,
                    initialCost: 0
                });
                start = hours[i].start;
                end = hours[i].end;
            }
        }
    
        groupedHours.push({
            start: start,
            end: end,
            initialCost: 0
        });

        console.log(groupedHours);

        groupedHours.forEach(group => {
            const endTime = new Date(group.end);
            if (endTime.getHours() === 0) {
                let endString = endTime.getFullYear() + '-' + (endTime.getMonth() + 1) + '-' + endTime.getDate() + 'T00:00:00';
                group.end = endString;
            }
        }
        );

        return groupedHours;

    }

    const handleReserve = async () => {
        if (selectedHours.length > 0) {
            const groupedHours = groupSelectedHours(selectedHours);

            const reservation = {
                spotId: spot,
                lotId: lot,
                start: null,
                end: null,
            };

            for (let i = 0; i < groupedHours.length; i++) {
                reservation.start = groupedHours[i].start;
                reservation.end = groupedHours[i].end;
                reservation.initialCost = groupedHours[i].initialCost;
                try {
                    const response = await fetch('http://localhost:8080/reservations/create', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json',
                            'Authorization': `Bearer ${token}`
                        },
                        body: JSON.stringify(reservation)
                    });
                    if (response.ok) {
                        console.log('Reservation created');
                    }
                } catch (error) {
                    console.error('Error creating reservation', error);
                    alert('Error creating reservation');
                }
            }
        }
        onClose();
        return;
    }

    return (
        <div className='reservationWrapper' onClick={(e) => handleClose(true, e)}>
            <div className='reservation' onClick={(e) => handleClose(false, e)}>
                <div className='day-selection'>
                    {days.map(day => (
                        <div
                            key={day.date}
                            className={`day-button ${selectedDay.date === day.date ? 'selected' : ''}`} 
                            onClick={UpdateSelectedDay(day)}>
                            <span>{day.day}</span>
                            <span>{day.date}/{day.month}</span>
                        </div>
                    ))}
                </div>
                <div className='hours-selection'>
                    {selectedDay.hours.map(hour => (
                        <div 
                            key={hour} 
                            className={`
                                ${isReserved(selectedDay ,hour) ? 'reserved-button' : 'not-reserved-button'}
                                ${selectedHours.some(selectedHour => selectedHour.start === selectedDay.fullDate + 'T' + hour.split(' ')[0]+':00')  ? 'selected-hour-button' : ''}
                            `}
                            onClick={selectHours(selectedDay, hour)}
                        >
                            <span className='hour-details'>{hour}</span>
                            <span className='reserved-details'>
                                {isReserved(selectedDay ,hour) ? 'Reserved' : ''}
                            </span>
                        </div>
                    ))}
                </div>
                <button
                    className='reserve-button'
                    onClick={handleReserve}
                >
                    Reserve
                </button>
            </div>
        </div>
    );
}

export default Reservation;