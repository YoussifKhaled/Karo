import React, { useState } from 'react';
import './Wallet.css';
import Header from '../../components/header/Header';

function Wallet() {
    const [balance, setBalance] = useState(0);

    const addBalance = (amount) => {
        setBalance(balance + amount);
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
                    <button onClick={() => addBalance(10000)}>Add ج.م 1,0000</button>
                </div>
            </div>
        </>
    );
}

export default Wallet;