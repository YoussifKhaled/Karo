import './NotificationCenter.css';
import React, { useState, useEffect } from 'react';
import Header from '../../components/header/Header';
import Notification from '../../components/notification/Notification';

function NotificationCenter() {
    const [notifications, setNotifications] = useState([]); // State to store notifications
    const token = localStorage.getItem('token')

    // useEffect(() => {
    //     const fetchBooks = async () => {
    //         const response = await fetch(
    //                 `http://localhost:8080/books?page=${page}&size=${PAGESIZE}`, {
    //                 method: 'GET',
    //                 headers: {"Authorization": `Bearer ${token}`},
    //             }
    //         );
    //         const data = await response.json();
    //         console.log(data)
    //         setBooks((prev) => [...prev, ...data]);
    //     };
    //     fetchBooks();
    // }, [page, token]);

    useEffect(() => {
        fetch(`http://localhost:8080/notifications`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('authToken')}`,
                'Content-Type': 'application/json'
            }
        })
        .then(response => {
            return response.json()
        
        })
        .then(data => setNotifications(data))
        .catch(error => console.error("Error fetching notifications:", error));
    }, [token]);

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