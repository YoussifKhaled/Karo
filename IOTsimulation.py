import mysql.connector
import random
import time


DB_CONFIG = {
    'host': 'localhost',
    'user': 'karo_admin',
    'password': 'karo13',
    'database': 'karo'
}

def update_parking_spot_status(cursor):
    """
    Randomly updates the status of parking spots to simulate IoT updates.
    """
    try:
        
        cursor.execute("SELECT spot_id, lot_id FROM parking_spot")
        spots = cursor.fetchall()

       
        for spot_id, lot_id in spots:
            new_status = random.choice(['available', 'occupied', 'reserved'])
            new_sensor_status = random.choice(['active', 'inactive', 'faulty'])

            cursor.execute(
                """
                UPDATE parking_spot
                SET spot_status = %s, sensor_status = %s
                WHERE spot_id = %s AND lot_id = %s
                """,
                (new_status, new_sensor_status, spot_id, lot_id)
            )
        print(f"{len(spots)} parking spots updated.")
    except Exception as e:
        print(f"Error updating parking spots: {e}")

def main():
    """
    Main function to connect to the database and periodically update parking spots.
    """
    try:
        connection = mysql.connector.connect(**DB_CONFIG)
        cursor = connection.cursor()

        print("Starting parking spot status simulator...")
        while True:
            update_parking_spot_status(cursor)
            connection.commit()
            time.sleep(60)  # Wait 5 seconds before the next update
    except KeyboardInterrupt:
        print("Simulator stopped.")
    except Exception as e:
        print(f"Database connection error: {e}")
    finally:
        if connection.is_connected():
            cursor.close()
            connection.close()
            print("Database connection closed.")

if __name__ == "__main__":
    main()
