import "./Header.css";
import { useNavigate } from 'react-router-dom';
import HomeIcon from '@mui/icons-material/Home';
import AccountCircleIcon from '@mui/icons-material/AccountCircle';
import NotificationsIcon from '@mui/icons-material/Notifications';

function Header ({title}) {

    const navigate = useNavigate();

  return (
    <div className="header">
        <HomeIcon className="home-icon" sx={{ fontSize: 30 }}
        onClick={() => navigate('/home')}
        />
        <span className="karo">{title}</span>
        <NotificationsIcon className="notification-icon" sx={{ fontSize: 30 }}
        onClick={() => navigate('/notifications')}
        />
        <AccountCircleIcon className="account-icon" sx={{ fontSize: 30 }}/>
    </div>
    );
}

export default Header;