import './App.css';
import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';

import Login from './pages/login/Login';
import Signup from './pages/signup/Signup';
import Home from './pages/home/Home';
import ParkingLot from './pages/parkinglot/ParkingLot';


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
          </Routes> 
        </main>
      </Router>
    </div>
  );
}

export default App;
