import "./Header.css";
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import HomeIcon from '@mui/icons-material/Home';
import AccountCircleIcon from '@mui/icons-material/AccountCircle';
import NotificationsIcon from '@mui/icons-material/Notifications';

function Header ({title}) {

  const navigate = useNavigate();
  const [dropdown, setDropdown] = useState(false);

  const logOut = () => {
    localStorage.removeItem('token');
    navigate('/');
  }

  const goToWallet = () => {
    navigate('/wallet');
  }

  return (
    <>
      <div className="header">
          <HomeIcon className="home-icon" sx={{ fontSize: 30 }}
          onClick={() => navigate('/home')}
          />
          <span className="karo">{title}</span>
          <NotificationsIcon className="notification-icon" sx={{ fontSize: 30 }}
          onClick={() => navigate('/notifications')}
          />
          <AccountCircleIcon
            className="account-icon" sx={{ fontSize: 30 }}
            onClick={() => setDropdown(!dropdown)}
          />
      </div>
      {dropdown && (
        <ul className="dropdown">
            <li className="dropdown-element" onClick={goToWallet}>Wallet</li>
            <li className="dropdown-element" onClick={logOut}>Logout</li>
        </ul>
      )}
    </>
    );
}

export default Header;