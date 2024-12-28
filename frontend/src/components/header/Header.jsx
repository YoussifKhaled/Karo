import "./Header.css";
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import HomeIcon from '@mui/icons-material/Home';
import AccountCircleIcon from '@mui/icons-material/AccountCircle';
import NotificationsIcon from '@mui/icons-material/Notifications';

function Header ({title,mode}) {

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
      {
        mode === 0 && // Driver
        <>
          <div className="header">
              <HomeIcon 
                className="home-icon" sx={{ fontSize: 30 }}
                onClick={() => navigate('/home')}
              />
              <span className="karo">{title}</span>
              <NotificationsIcon 
                className="notification-icon" sx={{ fontSize: 30 }}
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
      }
      {
        mode === 1 && // Manager
        <>
          <div className="header">
              <HomeIcon 
                className="home-icon" sx={{ fontSize: 30 }}
                onClick={() => navigate('/home')}
              />
              <span className="karo">{title}</span>
              <span className="create-parking-lot" onClick={() => navigate('/create-parking-lot')}>Create Parking Lot</span>
              <span className="manager-logout" onClick={logOut}>Logout</span>
          </div>
        </>
      }
      {
        mode === 2 && // Admin
        <>
          <div className="header">
              <HomeIcon 
                className="home-icon" sx={{ fontSize: 30 }}
                onClick={() => navigate('/home')}
              />
              <span className="karo">{title}</span>
              <button className="create-manager" onClick={() => navigate('/create-manager')}>Create Manager</button>
              <button className="admin-logout" onClick={logOut}>Logout</button>
          </div>
        </>
      }
    </>
    );
}

export default Header;