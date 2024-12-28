import './App.css';
import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';

import Login from './pages/login/Login';
import Signup from './pages/signup/Signup';
import Home from './pages/home/Home';
import ParkingLot from './pages/parkinglot/ParkingLot';
import NotificationCenter from './pages/notificationcenter/NotificationCenter';
import Wallet from './pages/wallet/Wallet';
import CreateManager from './pages/create-manager/CreateManager';
import CreateParkingLot from './pages/create-parking-lot/CreateParkingLot';

function App() {
  return (
    <div className="App">
      <Router>
        <main>
          <Routes>
            <Route path="/" element={<Login />} />
            <Route path="/signup" element={<Signup />} />
            <Route path="/home" element={<Home />} />
            <Route path="/parking-lot/:lotId" element={<ParkingLot />} />
            <Route path="/notifications" element={<NotificationCenter />} />
            <Route path="/wallet" element={<Wallet />} />
            <Route path="/create-manager" element={<CreateManager />} />
            <Route path="/create-parking-lot" element={<CreateParkingLot />} />
          </Routes>
        </main>
      </Router>
    </div>
  );
}

export default App;
