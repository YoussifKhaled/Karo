import './ReservationModal.css';
import { useState,useEffect } from 'react';
    
const fetchReservations = async (lotId, spotId) => {
    const response = await fetch(
        `http://localhost:8080/reservations/by-lot-and-spot/future?lotId=${lotId}&spotId=${spotId}`, {
        method: 'GET',
    });
    const data = await response.json();
    return Array.isArray(data) ? data : [];
};
    
function Reservation ({ lot, spot, onClose }) {

    const [reservations, setReservations] = useState([]);

    useEffect(() => {
        const getReservations = async () => {
            const data = await fetchReservations(lot, spot);
            // loop through data and divide each reservation into hours
            // for example: reservation from 10:00 to 12:00 will be divided into 10:00-11:00 and 11:00-12:00
            // then set the reservations
            for (let i = 0; i < data.length; i++) {
                const start = new Date(data[i].start);
                const end = new Date(data[i].end);
                const hours = [];
                for (let j = start.getHours(); j < end.getHours(); j++) {
                    hours.push((j<10?'0':'') + j + ':00 - ' + (j + 1) + ':00');
                }
                data[i].hours = hours;
            }
            setReservations(data);
            console.log(data);
        };
        getReservations();
    }, [lot, spot]);

    const now = new Date();
    const daysOfWeek = ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'];
    const [selectedHours, setSelectedHours] = useState([]);

    const getHours = (from) => {
        const hours = [];
        for (let i = from; i < 24; i++) {
            hours.push((i<10?'0':'') + i + ':00 - ' + (i + 1) + ':00');
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
        const lookFor = day.fullDate + 'T' + startHour;

        if (selectedHours.includes(lookFor)) {
            setSelectedHours(selectedHours.filter(h => h !== lookFor));
            return;
        }

        setSelectedHours([...selectedHours, lookFor]);
    }

    // const groupSelectedHours = (hours) => {
    // }

    const handleReserve = async () => {
        if (selectedHours.length > 0) {
            //TODO: send reservation request
            // const groupedHours = groupSelectedHours(selectedHours);
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
                                ${selectedHours.includes(selectedDay.fullDate + 'T' + hour.split(' ')[0]+':00') ? 'selected-hour-button' : ''}
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