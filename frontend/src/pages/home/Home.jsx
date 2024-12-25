import './Home.css';
import React from 'react';
import MapComponent from '../../components/map/Map';
import Header from '../../components/header/Header';

function Home() {
  return (
    <div className="Home">
        <Header title="Karo" />
        <div className="map-container">
            <MapComponent />
        </div>
    </div>
  );
}

export default Home;