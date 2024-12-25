import React from "react";
import "./Notification.css";
import ErrorIcon from "@mui/icons-material/Error"; // Icon for overdue
import ScheduleIcon from "@mui/icons-material/Schedule"; // Icon for reservation in 15 minutes
import CheckCircleIcon from "@mui/icons-material/CheckCircle"; // Icon for reservation confirmed

function Notification({ type, message, dateTime }) {
    let icon, className;

    // Determine icon and style based on the type of notification
    switch (type) {
        case "overdue":
            icon = <ErrorIcon className="notification-icon overdue" />;
            className = "notification overdue";
            break;
        case "reservationSoon":
            icon = <ScheduleIcon className="notification-icon soon" />;
            className = "notification soon";
            break;
        case "reservationConfirmed":
            icon = <CheckCircleIcon className="notification-icon confirmed" />;
            className = "notification confirmed";
            break;
        default:
            icon = null;
            className = "notification";
    }

    return (
        <div className={className}>
            <div className="notification-left">
                {icon}
                <span className="notification-message">{message}</span>
            </div>
            <span className="notification-dateTime">{dateTime}</span>
        </div>
    );
}

export default Notification;
