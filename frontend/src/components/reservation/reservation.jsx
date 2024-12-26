import './Reservation.css';
import { useState } from 'react';

function Reservation ({ spot, onClose }) {


    // Mock reservations fetched from back
    const reservationsFromBack = [
        { res_id: 1, start: '2024-12-26T10:00:00', end: '2024-12-26T11:00:00' },
        { res_id: 2, start: '2024-12-26T12:00:00', end: '2024-12-26T13:00:00' },
        { res_id: 3, start: '2024-12-26T14:00:00', end: '2024-12-26T15:00:00' },
        { res_id: 4, start: '2024-12-26T16:00:00', end: '2024-12-26T17:00:00' },
        { res_id: 5, start: '2024-12-27T18:00:00', end: '2024-12-27T19:00:00' },
        { res_id: 6, start: '2024-12-28T20:00:00', end: '2024-12-28T21:00:00' },
        { res_id: 7, start: '2024-12-28T22:00:00', end: '2024-12-28T23:00:00' },
        { res_id: 8, start: '2024-12-29T10:00:00', end: '2024-12-29T11:00:00' },
        { res_id: 9, start: '2024-12-30T12:00:00', end: '2024-12-30T13:00:00' },
        { res_id: 10, start: '2024-12-30T14:00:00', end: '2024-12-30T15:00:00' }
    ]

    const now = new Date();
    const daysOfWeek = ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'];
    
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
        const startHour = hour.split(' ')[0]+':00';
        const lookFor = day.fullDate + 'T' + startHour;
        return reservationsFromBack.some(res => res.start === lookFor);
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
                        <div key={hour} className='hour-button'>
                            <span>
                                {isReserved(selectedDay ,hour) ? 'Reserved' : 'Open'}
                            </span>
                        </div>
                    ))}
                </div>
            </div>
        </div>
    );
}

export default Reservation;