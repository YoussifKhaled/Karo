import './Home.css';
import React from 'react';
import MapComponent from '../../components/map/Map';
import Header from '../../components/header/Header';

// mode: Driver:0 or Manager:1 or Admin:2
const mode = 2;

function Home() {
  return (
    <div className="Home">
        <Header title="Karo" mode={mode} />
        <div className="map-container">
            <MapComponent mode={mode} />
        </div>
    </div>
  );
}

export default Home;