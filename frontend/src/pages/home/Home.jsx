import './Home.css';
import { useState } from 'react';
import MapComponent from '../../components/map/Map';
import Header from '../../components/header/Header';


// mode: Driver:0 or Manager:1 or Admin:2
const mode = 2;

function Home() {

  const [topDrivers, setTopDrivers] = useState([]);
  const [topParkingLots, setTopParkingLots] = useState([]);
  const [systemManagers, setSystemManagers] = useState([]);

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
              
              </ul>
              <ul className="admin-list">
                <div className="list-title">Top Parking Lots</div>

              </ul>
              <ul className="admin-list">
                <div className="list-title">System Managers</div>

              </ul>
          </div>
        }
    </div>
  );
}

export default Home;