import './ParkingLot.css';
import { useState } from 'react';
import { useParams } from 'react-router-dom';
import Header from '../../components/header/Header';
import EvStationIcon from '@mui/icons-material/EvStation';
import AccessibleIcon from '@mui/icons-material/Accessible';
import Reservation from './../../components/reservation/Reservation';

const parkingSpots = [
    { id: 1, status: 'open', type: 'regular' },
    { id: 2, status: 'open', type: 'disabled' },
    { id: 3, status: 'open', type: 'regular' },
    { id: 4, status: 'open', type: 'regular' },
    { id: 5, status: 'open', type: 'ev' },
    { id: 6, status: 'open', type: 'regular' },
    { id: 7, status: 'open', type: 'regular' },
    { id: 8, status: 'reserved', type: 'regular' },
    { id: 9, status: 'open', type: 'regular' },
    { id: 10, status: 'open', type: 'disabled' },
    { id: 11, status: 'reserved', type: 'regular' },
    { id: 12, status: 'open', type: 'ev' },
    { id: 13, status: 'open', type: 'regular' },
    { id: 14, status: 'reserved', type: 'disabled' },
    { id: 15, status: 'open', type: 'regular' }
]

function ParkingLot() {

    const { lotId } = useParams();
    const [reservingSpot, setReservingSpot] = useState(null);

    return (
        <div className='parking-lot'>
            <Header title={`Parking Lot ${lotId}`}/>
            {reservingSpot &&
                <Reservation spot={reservingSpot} onClose={() => setReservingSpot(null)} />
            }
            <ul className='parking-spots'>
                {parkingSpots.map(parkingSpot => (
                    <li key={parkingSpot.id} className={`parking-spot`} onClick={() => setReservingSpot(parkingSpot)}>
                        <span className='spot-title'>Spot {parkingSpot.id}</span>
                        {parkingSpot.type === 'ev' && <EvStationIcon style={{ color: 'green' }} className="spot-type" />}
                        {parkingSpot.type === 'disabled' && <AccessibleIcon className="spot-type"/>}
                    </li>
                ))}
            </ul>
        </div>
    );
}

export default ParkingLot;