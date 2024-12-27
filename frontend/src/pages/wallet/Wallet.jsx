import React, { useEffect, useState } from 'react';
import './Wallet.css';
import Header from '../../components/header/Header';

function Wallet() {
    const [balance, setBalance] = useState();
    const token = localStorage.getItem("token");

    useEffect(() => {
        const getCurrentUserBalance = async () => {
          const response = await fetch("http://localhost:8080/users/wallet",{
            method: 'GET',
            headers: { 
              "Authorization": `Bearer ${token}`,
              "Content-Type": "application/json",
            }
          })
          const data = await response.json();
          console.log("balance: "+data);
          setBalance(data);
        }
        getCurrentUserBalance();
      }, [token]);

    const addBalance = async(amount) => {
        // setBalance(balance + amount);
        const response = await fetch(`http://localhost:8080/users/add-balance?balance=${amount}`,{
            method: 'POST',
            headers: { 
              "Authorization": `Bearer ${token}`,
              "Content-Type": "application/json",
            }
          })
          const data = await response.json();
          console.log("current balance: "+data);
          setBalance(data);
    };

    return (
        <>
            <Header title="Wallet" />
            <div className="wallet">
                <p className="balance">Current Balance: {balance} ج.م</p>
                <div className="moneyButtons">
                    <button onClick={() => addBalance(10)}>Add 10 ج.م</button>
                    <button onClick={() => addBalance(100)}>Add ج.م 100</button>
                    <button onClick={() => addBalance(1000)}>Add ج.م 1,000</button>
                    <button onClick={() => addBalance(10000)}>Add ج.م 10,000</button>
                </div>
            </div>
        </>
    );
}

export default Wallet;