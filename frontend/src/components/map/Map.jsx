import L from 'leaflet';
import 'leaflet/dist/leaflet.css';
// import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { MapContainer, TileLayer, Marker, Tooltip } from 'react-leaflet';

const customIcon = new L.Icon({
    iconUrl: require('./../../marker-icon.webp'),
    shadowUrl: require('leaflet/dist/images/marker-shadow.png'),
    iconSize: [40, 40],
    shadowSize: [45, 45],
    tooltipAnchor: [20, 0],
    tooltipSize: [40, 40],
});

// Generate random parking lots
const parkingLots = Array.from({ length: 10 }, (_, index) => ({
    id: index,
    name: `Parking Lot ${index + 1}`,
    latitude: 31.207 + Math.random() / 100,
    longitude: 29.924 + Math.random() / 100,
}));

function MapComponent () {
    const navigate = useNavigate();

    const handleMarkerClick = (lotId) => {
        navigate(`/parking-lot/${lotId+1}`);
    }

    return (
        <>
            <MapContainer center={[31.207, 29.924]} zoom={15} style={{ height: '100%', width: '100%' }}>
                <TileLayer
                    url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                    attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
                />
                {parkingLots.map((lot) => (
                    <Marker 
                        key={lot.id} 
                        position={[lot.latitude, lot.longitude]}
                        icon={customIcon}
                        eventHandlers={{
                            click: () => handleMarkerClick(lot.id),
                        }}
                        >
                        
                        <Tooltip>{lot.name}</Tooltip>
                    </Marker>
                ))}
            </MapContainer>
        </>
    );
}

export default MapComponent;