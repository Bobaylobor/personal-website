import React from 'react';
import { Link, Outlet } from 'react-router-dom';

const ProjectsList = () => {
    return (
        <div>
            <Link to="/">back</Link>
            <Link to="/projects/colormap">Color Map</Link>
        </div>
    );
};

export default ProjectsList;
