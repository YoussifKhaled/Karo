import random
from datetime import datetime, timedelta
from faker import Faker
import mysql.connector

# Configure Faker
fake = Faker()

# MySQL Database Configuration
DB_CONFIG = {
    'host': 'localhost',
    'user': 'karo_admin',
    'password': 'karo13',
    'database': 'karo'
}

# Connect to MySQL
connection = mysql.connector.connect(**DB_CONFIG)
cursor = connection.cursor()

def insert_user(name, email, password_hash, role):
    query = """
        INSERT INTO user (name, email, password_hash, role)
        VALUES (%s, %s, %s, %s)
    """
    cursor.execute(query, (name, email, password_hash, role))
    connection.commit()
    return cursor.lastrowid

def insert_driver(user_id, license_plate_number, balance):
    query = """
        INSERT INTO driver (user_id, license_plate_number, balance)
        VALUES (%s, %s, %s)
    """
    cursor.execute(query, (user_id, license_plate_number, balance))
    connection.commit()

def insert_parking_lot(manager_id, longitude, latitude, capacity, safe):
    query = """
        INSERT INTO parking_lot (manager_id, longitude, latitude, capacity, safe)
        VALUES (%s, %s, %s, %s, %s)
    """
    cursor.execute(query, (manager_id, longitude, latitude, capacity, safe))
    connection.commit()
    return cursor.lastrowid

def insert_parking_spot(spot_id, lot_id, spot_type, sensor_id, spot_status, sensor_status, price):
    query = """
        INSERT INTO parking_spot (spot_id, lot_id, type, sensor_id, spot_status, sensor_status, price)
        VALUES (%s, %s, %s, %s, %s, %s, %s)
    """
    cursor.execute(query, (spot_id, lot_id, spot_type, sensor_id, spot_status, sensor_status, price))
    connection.commit()

def insert_reservation(driver_id, spot_id, lot_id, start, end, violation, initial_cost):
    query = """
        INSERT INTO reservation (driver_id, spot_id, lot_id, start, end, violation, initial_cost)
        VALUES (%s, %s, %s, %s, %s, %s, %s)
    """
    cursor.execute(query, (driver_id, spot_id, lot_id, start, end, violation, initial_cost))
    connection.commit()

def insert_notification(sent_at, notif_type, content):
    query = """
        INSERT INTO notification (sent_at, type, content)
        VALUES (%s, %s, %s)
    """
    cursor.execute(query, (sent_at, notif_type, content))
    connection.commit()
    return cursor.lastrowid

def insert_user_notification(user_id, notification_id):
    query = """
        INSERT INTO user_notification (user_id, notification_id)
        VALUES (%s, %s)
    """
    cursor.execute(query, (user_id, notification_id))
    connection.commit()

# Generate and Insert Data
users = []
drivers = []
parking_lots = []
parking_spots = []
reservations = []
notifications = []

# Create Users
for _ in range(10):
    name = fake.name()
    email = fake.unique.email()
    password_hash = fake.sha256()
    role = random.choice(['regular', 'admin', 'driver'])
    user_id = insert_user(name, email, password_hash, role)
    users.append((user_id, role))

# Create Drivers
for user_id, role in users:
    if role == 'driver':
        license_plate_number = fake.unique.license_plate()
        balance = random.randint(0, 5000)
        insert_driver(user_id, license_plate_number, balance)
        drivers.append(user_id)

center_long = 31.207
center_lat = 29.924

offset_range = 2.0

# Create Parking Lots
for _ in range(5):
    manager_id = random.choice([user_id for user_id, role in users if role == 'admin'])
    longitude = center_long + random.uniform(-offset_range, offset_range)
    latitude = center_lat + random.uniform(-offset_range, offset_range)
    capacity = random.randint(10, 50)
    safe = random.randint(1, capacity)
    lot_id = insert_parking_lot(manager_id, longitude, latitude, capacity, safe)
    parking_lots.append(lot_id)

# Create Parking Spots
spot_id = 1
for lot_id in parking_lots:
    for _ in range(random.randint(5, 20)):
        spot_type = random.choice(['regular', 'disabled', 'EV charging'])
        sensor_id = random.randint(1000, 9999)
        spot_status = random.choice(['occupied', 'available', 'reserved'])
        sensor_status = random.choice(['active', 'inactive', 'faulty'])
        price = round(random.uniform(1.0, 20.0), 2)
        insert_parking_spot(spot_id, lot_id, spot_type, sensor_id, spot_status, sensor_status, price)
        parking_spots.append((spot_id, lot_id))
        spot_id += 1

# Create Reservations
for _ in range(15):
    driver_id = random.choice(drivers)
    spot_id, lot_id = random.choice(parking_spots)
    start = datetime.now() + timedelta(days=random.randint(1, 7))
    end = start + timedelta(hours=random.randint(1, 8))
    violation = random.choice(['Not Shown', 'Over Stayed', None])
    initial_cost = round(random.uniform(5.0, 50.0), 2)
    insert_reservation(driver_id, spot_id, lot_id, start, end, violation, initial_cost)

# Create Notifications
for _ in range(20):
    sent_at = datetime.now() - timedelta(days=random.randint(0, 30))
    notif_type = random.choice(['Payment Reminder', 'Spot Availability', 'Reservation Confirmation', 'Sensor Alert', 'General Update'])
    content = fake.text(max_nb_chars=200)
    notification_id = insert_notification(sent_at, notif_type, content)
    notifications.append(notification_id)

# Create User Notifications
for _ in range(30):
    user_id = random.choice([user_id for user_id, _ in users])
    notification_id = random.choice(notifications)
    insert_user_notification(user_id, notification_id)

# Close Connection
cursor.close()
connection.close()

print("Fake data inserted successfully!")
