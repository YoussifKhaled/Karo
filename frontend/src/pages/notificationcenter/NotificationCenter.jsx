import './NotificationCenter.css';
import React, { useState, useEffect } from 'react';
import Header from '../../components/header/Header';
import Notification from '../../components/notification/Notification';

function NotificationCenter() {
    const [notifications, setNotifications] = useState([]); // State to store notifications
    const userId = 1

    useEffect(() => {
        fetch(`http://localhost:8080/notifications/${userId}`)
        .then(response => {
            console.log(response)
            return response.json()
        
        })
        .then(data => setNotifications(data))
        .catch(error => console.error("Error fetching notifications:", error));
    }, [userId]);

    return (
        <div>
            <Header title="Notification Center" />
            <div className="notification-center">
                {notifications.length > 0 ? (
                    notifications.map((notification, index) => (
                    <Notification 
                        key={index}
                        type={notification.type}
                        message={notification.content}
                        dateTime={notification.sentAt}
                    />
                    ))
                ) : (
                    <p>No notifications available</p>
                )}
            </div>
        </div>
    );
}

export default NotificationCenter;