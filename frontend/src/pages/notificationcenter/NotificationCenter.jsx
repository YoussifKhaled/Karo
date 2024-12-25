import './NotificationCenter.css';
import React from 'react';
import Header from '../../components/header/Header';
import Notification from '../../components/notification/Notification';

function NotificationCenter() {
  return (
    <div>
        <Header title="Notification Center" />
        <div className="notification-center">
            <Notification 
                type="overdue" 
                message="Your reservation is overdue! Please check." 
                dateTime="Dec 25, 2024 - 10:00 AM"
            />
            <Notification 
                type="reservationSoon" 
                message="You have a reservation in 15 minutes." 
                dateTime="Dec 25, 2024 - 10:15 AM"
            />
            <Notification 
                type="reservationConfirmed" 
                message="Your reservation has been confirmed." 
                dateTime="Dec 25, 2024 - 9:00 AM"
            />
        </div>
    </div>
  );
}

export default NotificationCenter;