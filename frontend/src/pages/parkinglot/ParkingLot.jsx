import React from 'react';
import { useParams } from 'react-router-dom';
import Header from '../../components/header/Header';

function ParkingLot() {

    const { lotId } = useParams();

    return (
        <div>
            <Header title={`Parking Lot ${lotId}`}/>
        </div>
    );
}

export default ParkingLot;