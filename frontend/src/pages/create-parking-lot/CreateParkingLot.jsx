import './CreateParkingLot.css';
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { MapContainer, TileLayer, Marker, Tooltip, useMapEvents } from 'react-leaflet';
import L from 'leaflet';

const customIcon = new L.Icon({
    iconUrl: require('./../../assets/marker-icon.webp'),
    shadowUrl: require('leaflet/dist/images/marker-shadow.png'),
    iconSize: [40, 40],
    shadowSize: [45, 45],
    tooltipAnchor: [20, 0],
    tooltipSize: [40, 40],
});

function CreateParkingLot() {
    
    const [capacity, setCapacity] = useState('');
    const [price, setPrice] = useState('');
    const [position, setPosition] = useState(null);

    const navigate = useNavigate();

    const MapClickHandler = () => {
        useMapEvents({
            click(e) {
                setPosition(e.latlng);
            }
        });
        return position === null ? null : (
            <Marker position={position} icon={customIcon}>
                <Tooltip>Lot Position</Tooltip>
            </Marker>
        );
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!position || !capacity || !price) {
            alert('Please fill all fields and select a location on the map.');
            return;
        }

        const parkingLot = {
            capacity: Number(capacity),
            price: Number(price),
            longitude: position.lng,
            latitude: position.lat,
            safe: 0,
            managerId: 1
        };

        try {
            const response = await fetch('http://localhost:8080/lot/create', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${localStorage.getItem('token')}`,
                },
                body: JSON.stringify(parkingLot),
            });

            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.message || 'Failed to create parking lot.');
            }

            alert('Parking lot created successfully!');
            // Reset form
            setCapacity('');
            setPrice('');
            setPosition(null);
            navigate('/home');
        } catch (error) {
            console.error('Error creating parking lot:', error);
            alert(error.message || 'Failed to create parking lot.');
        }
    };

    return (
        <div className="create-parking-lot-container">
            <form onSubmit={handleSubmit}>
                <div className="create-pl-map-container">
                    <MapContainer center={[31.207, 29.924]} zoom={13} style={{ height: '100%', width: '100%' }}>
                        <TileLayer
                            url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                            attribution="&copy; OpenStreetMap contributors"
                        />
                        <MapClickHandler />
                    </MapContainer>
                </div>
                <div className="create-pl-form-group">
                    <label>Capacity:</label>
                    <input
                        type="number"
                        value={capacity}
                        onChange={(e) => setCapacity(e.target.value)}
                        required
                    />
                </div>
                <div className="create-pl-form-group">
                    <label>Standard Price:</label>
                    <input
                        type="number"
                        step="0.01"
                        value={price}
                        onChange={(e) => setPrice(e.target.value)}
                        required
                    />
                </div>
                
                <button type="submit" className="create-pl-submit-button">Create Parking Lot</button>
            </form>
        </div>
    );
}

export default CreateParkingLot;