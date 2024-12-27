import './ParkingLot.css';
import { useState,useEffect,useRef } from 'react';
import { useCallback } from 'react';
import { useParams } from 'react-router-dom';
import Header from '../../components/header/Header';
import EvStationIcon from '@mui/icons-material/EvStation';
import AccessibleIcon from '@mui/icons-material/Accessible';
import Reservation from '../../components/reservation/ReservationModal';

function ParkingLot() {

    const { lotId } = useParams();
    const numericLotId = Number(lotId);
    const [reservingSpot, setReservingSpot] = useState(null);

    const pageSize = 11;
    const [page, setPage] = useState(0);
    const [parkingSpots, setParkingSpots] = useState([]);

    const scrollContainerRef = useRef(null);
    const prevScrollRef = useRef(0);

    const fetchParkingSpots = useCallback(async (reset = false) => {
        const currentPage = reset ? 0 : page;
        const response = await fetch(
            `http://localhost:8080/spot/lot-spots/${numericLotId}?limit=${pageSize}&offset=${currentPage * pageSize}`, {
            method: 'GET',
            headers: {
                "Authorization": `Bearer ${localStorage.getItem('token')}`
            }
        });
        const data = await response.json();
        if (reset) {
            setParkingSpots([...data]);
            setPage(0);
        } else {
            setParkingSpots((prev) => [...prev, ...data]);
        }
    }, [page, numericLotId]);

    useEffect(() => {
        fetchParkingSpots(page === 0);
    }, [page, fetchParkingSpots]);

    const handleScroll = (e) => {
        const { scrollTop, scrollHeight, clientHeight } = e.target;
        if (scrollTop > prevScrollRef.current && scrollTop + clientHeight + 1 >= scrollHeight) {
            setPage((prev) => prev + 1);
        }
        prevScrollRef.current = Math.max(prevScrollRef.current, scrollTop);
    }

    useEffect(() => {
        const scrollContainer = scrollContainerRef.current;
        if (scrollContainer) {
            scrollContainer.addEventListener('scroll', handleScroll);
        return () => scrollContainer.removeEventListener('scroll', handleScroll);
        }
    }, []);

    return (
        <div className='parking-lot'>
            {reservingSpot &&
                <Reservation lot={numericLotId} spot={reservingSpot.spotId} onClose={() => setReservingSpot(null)} />
            }
            <Header title={`Parking Lot ${lotId}`}/>
            <ul className='parking-spots' ref={scrollContainerRef}>
                {parkingSpots.map(parkingSpot => (
                    <li key={parkingSpot.spotId} className={`parking-spot`} onClick={() => setReservingSpot(parkingSpot)}>
                        <span className='spot-title'>Spot {parkingSpot.spotId}</span>
                        {parkingSpot.type === 'EV charging' && <EvStationIcon style={{ color: 'green' }} className="spot-type" />}
                        {parkingSpot.type === 'disabled' && <AccessibleIcon className="spot-type"/>}
                    </li>
                ))}
            </ul>
        </div>
    );
}

export default ParkingLot;