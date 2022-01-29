import './App.css';
import { BrowserRouter as Router, Route, Routes, Link } from 'react-router-dom';
import Home from './components/Home';
import ProjectsList from './components/ProjectsList';
import ColorMap from './components/projects/ColorMap';

function App() {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<Home />} />
                <Route path="projects">
                    <Route index={true} element={<ProjectsList />} />
                    <Route path="colormap" element={<ColorMap />} />
                </Route>
            </Routes>
        </Router>
    );
}

export default App;
