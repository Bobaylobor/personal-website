import React from 'react';
import { Link } from 'react-router-dom';

const Home = () => {
    return (
        <div>
            <h3>Baylor Warrick</h3>
            <Link to="/projects">Projects</Link>
        </div>
    );
};

export default Home;
