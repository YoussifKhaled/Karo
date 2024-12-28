import './ParkingLot.css';
import { useState,useEffect,useRef } from 'react';
import { useCallback } from 'react';
import { useParams } from 'react-router-dom';
import Header from '../../components/header/Header';
import EvStationIcon from '@mui/icons-material/EvStation';
import AccessibleIcon from '@mui/icons-material/Accessible';
import Reservation from '../../components/reservation/ReservationModal';

function ParkingLot() {

    const token = localStorage.getItem('token');

    const { lotId } = useParams();
    const numericLotId = Number(lotId);
    const [reservingSpot, setReservingSpot] = useState(null);

    const pageSize = 11;
    const [page, setPage] = useState(0);
    const [parkingSpots, setParkingSpots] = useState([]);

    const scrollContainerRef = useRef(null);
    const prevScrollRef = useRef(0);

    const [mode, setMode] = useState(0);

    const [spotsStatus, setSpotsStatus] = useState([]);

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
        const fetchSpotsStatus = async () => {
            const response = await fetch(`http://localhost:8080/manager/lots/${numericLotId}/spots-status`, {
                method: 'GET',
                headers: {
                    "Authorization": `Bearer ${token}`,
                    "Content-Type": "application/json",
                }
            })
            const data = await response.json();
            setSpotsStatus(data);
            console.log("spots status: "+data);
        }
        fetchSpotsStatus();
    }, [token, numericLotId]);

    const fetchParkingSpots = useCallback(async (reset = false) => {
        const currentPage = reset ? 0 : page;
        const response = await fetch(
            `http://localhost:8080/spot/lot-spots/${numericLotId}?limit=${pageSize}&offset=${currentPage * pageSize}`, {
            method: 'GET',
            headers: {
                "Authorization": `Bearer ${localStorage.getItem('token')}`,
                "Content-Type": "application/json",
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
            {
                mode === 0 &&
                <>
                    {reservingSpot &&
                        <Reservation lot={numericLotId} spot={reservingSpot.spotId} onClose={() => setReservingSpot(null)} />
                    }
                    <Header title={`Parking Lot ${lotId}`} mode={mode}/>
                    <ul className='parking-spots' ref={scrollContainerRef}>
                        {parkingSpots.map(parkingSpot => (
                            <li key={parkingSpot.spotId} className={`parking-spot`} onClick={() => setReservingSpot(parkingSpot)}>
                                <span className='spot-title'>Spot {parkingSpot.spotId}</span>
                                {parkingSpot.type === 'EV charging' && <EvStationIcon style={{ color: 'green' }} className="spot-type" />}
                                {parkingSpot.type === 'disabled' && <AccessibleIcon className="spot-type"/>}
                            </li>
                        ))}
                    </ul>
                </>
            }
            {
                mode === 1 &&
                <>
                    <Header title={`Parking Lot ${lotId}`} mode={mode}/>
                    <div className="spots-status">
                        <span className="status">Free: {spotsStatus[0]?.count}</span>
                        <span className="status">Occupied: {spotsStatus[1]?.count}</span>
                        <span className="status">Reserved: {spotsStatus[2]?.count}</span>
                    </div>
                    <ul className='parking-spots' ref={scrollContainerRef}>
                        {parkingSpots.map(parkingSpot => (
                            <li key={parkingSpot.spotId} className={`parking-spot ${parkingSpot.spotStatus}`}>
                                <span className='spot-title'>Spot {parkingSpot.spotId}</span>
                                {parkingSpot.type === 'EV charging' && <EvStationIcon style={{ color: 'green' }} className="spot-type" />}
                                {parkingSpot.type === 'disabled' && <AccessibleIcon className="spot-type"/>}
                            </li>
                        ))}
                    </ul>
                </>
            }
        </div>
    );
}

export default ParkingLot;