import L from 'leaflet';
import 'leaflet/dist/leaflet.css';
import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { MapContainer, TileLayer, Marker, Tooltip } from 'react-leaflet';

const customIcon = new L.Icon({
    iconUrl: require('./../../assets/marker-icon.webp'),
    shadowUrl: require('leaflet/dist/images/marker-shadow.png'),
    iconSize: [40, 40],
    shadowSize: [45, 45],
    tooltipAnchor: [20, 0],
    tooltipSize: [40, 40],
});

function MapComponent () {

    const navigate = useNavigate();
    const [parkingLots, setParkingLots] = useState([]);

    const handleMarkerClick = (lotId) => {
        navigate(`/parking-lot/${lotId}`);
    }

    useEffect(() => {
        fetch('http://localhost:8080/lot/all',{
            method: 'GET',
            headers: {
                "Authorization": `Bearer ${localStorage.getItem('token')}`
            }
        })
            .then(response => response.json())
            .then(data => setParkingLots(data))
            .catch(error => console.error(error));
    }, []);

    return (
        <>
            <MapContainer center={[31.207, 29.924]} zoom={15} style={{ height: '100%', width: '100%' }}>
                <TileLayer
                    url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                    attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
                />
                {parkingLots.map((lot) => (
                    <Marker 
                        key={lot.lotId} 
                        position={[lot.latitude, lot.longitude]}
                        icon={customIcon}
                        eventHandlers={{
                            click: () => handleMarkerClick(lot.lotId),
                        }}
                        >
                        
                        <Tooltip>Lot {lot.lotId}</Tooltip>
                    </Marker>
                ))}
            </MapContainer>
        </>
    );
}

export default MapComponent;