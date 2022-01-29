import React, { useEffect, useState } from 'react';

const ColorMap = () => {
    const [name, setName] = useState(() => {
        const saved = localStorage.getItem("name");
        const initialValue = JSON.parse(saved);
        return initialValue || "";
    })

    const [result, setResult] = useState("");
    useEffect(() => {
        localStorage.setItem("name", JSON.stringify(name))
        try {
            setResult(eval(name));
        } catch (error) {

        }

    }, [name])

    return (
        <div>
            <form>
                <input
                    type="text"
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                    placeholder="Full name"
                    aria-label="fullname"
                />
                <input type="submit" value="Submit"></input>
            </form>
            <div>{result}</div>
        </div>
    );
};

export default ColorMap;
