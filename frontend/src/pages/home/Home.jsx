import './Home.css';
import { useState,useEffect,useCallback } from 'react';
import MapComponent from '../../components/map/Map';
import Header from '../../components/header/Header';
import { useLocation } from 'react-router-dom';

// mode: Driver:0 or Manager:1 or Admin:2

function Home() {

  const token = localStorage.getItem('token');
  const location = useLocation();

  const [mode, setMode] = useState(0)
  const [topDrivers, setTopDrivers] = useState([]);
  const [topParkingLots, setTopParkingLots] = useState([]);
  const [systemManagers, setSystemManagers] = useState([]);


  const fetchAllData = useCallback(async () => {
    try {
      const driverResponse = await fetch('http://localhost:8080/admin/top-users', {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`
        }
      });
      const driversData = await driverResponse.json();
      setTopDrivers(driversData);

      const lotsResponse = await fetch('http://localhost:8080/admin/top-lots', {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`
        }
      });
      const lotsData = await lotsResponse.json();
      setTopParkingLots(lotsData);
  
      const managerResponse = await fetch('http://localhost:8080/admin/all-managers', {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`
        }
      });
      const managerData = await managerResponse.json();
      setSystemManagers(managerData);
    }catch (error) {
      console.log(error);
    }
  }, [token]);

  useEffect(() => {
    if (location.state?.refresh) {
      console.log('Refresh triggered');
    }
  }, [location.state]);

  useEffect(() => {
    const getCurrentUserRole = async () => {
      const response = await fetch("http://localhost:8080/users/role",{
        method: 'GET',
        headers: { 
          "Authorization": `Bearer ${token}`,
          "Content-Type": "application/json",
        }
      })
      const data = await response.json();
      console.log("role: "+data);
      setMode(data);
    }
    getCurrentUserRole();
  }, [token]);

  useEffect(() => {
    // fetch top drivers
    fetchAllData();
  }, [fetchAllData]);

  const deleteDriver = async (userId) => {
    const response = await fetch(`http://localhost:8080/delete-user/${userId}`, {
      method: 'DELETE',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      }
    });
    const data = await response.json();
    console.log(data);
    fetchAllData();
  }

  const deleteParkingLot = async (lotId) => {
    const response = await fetch(`http://localhost:8080/lot/delete/${lotId}`, {
      method: 'DELETE',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      }
    });
    const data = await response.json();
    console.log(data);
    fetchAllData();
  }

  const deleteSystemManager = async (userId) => {
    const response = await fetch(`http://localhost:8080/delete-user/${userId}`, {
      method: 'DELETE',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      }
    });
    const data = await response.json();
    console.log(data);
    fetchAllData();
  }

  return (
    <div className="Home">
        <Header title="Karo" mode={mode} />
        {
          mode === 0 &&
          <div className="map-container">
              <MapComponent mode={mode} />
          </div>
        }
        {
          mode === 1 &&
          <div className="map-container">
              <MapComponent mode={mode} />
          </div>
        }
        {
          mode === 2 &&
          <div className="lists-container">
              <ul className="admin-list">
                <div className="list-title">Top Drivers</div>
                {
                  topDrivers.map(driver => (
                    <li key={driver.userId} className="driver-list-item">
                      <span>{driver.name}</span>
                      <span>{driver.reservationCount}</span>
                      <span
                        className="delete-icon"
                        onClick={() => deleteDriver(driver.userId)}
                      >
                        🗑
                      </span>
                    </li>
                  ))
                }
              </ul>
              <ul className="admin-list">
                <div className="list-title">Top Parking Lots</div>
                {
                  topParkingLots.map(lot => (
                    <li key={lot.lotId} className="lot-list-item">
                      <span>lot {lot.lotId}</span>
                      <span>{lot.revenue}</span>
                      <span 
                        className="delete-icon"
                        onClick={() => deleteParkingLot(lot.lotId)}
                      >
                        🗑
                      </span>
                    </li>
                  ))
                }
              </ul>
              <ul className="admin-list">
                <div className="list-title">System Managers</div>
                {
                  systemManagers.map(manager => (
                    <li key={manager.userId} className="manager-list-item">
                      <span>{manager.name}</span>
                      <span 
                        className="delete-icon"
                        onClick={() => deleteSystemManager(manager.userId)}
                      >
                        🗑
                      </span>
                    </li>
                  ))
                }
              </ul>
          </div>
        }
    </div>
  );
}

export default Home;