import React, { useState } from 'react';

const GRID_SIZE = 13;
const cells = Array.from({ length: GRID_SIZE * GRID_SIZE });

export const BackgroundRippleEffect = () => {
    const [activeCell, setActiveCell] = useState(null);
    const center = Math.floor(GRID_SIZE / 2);

    return (
        <div className="background-ripple-effect" aria-hidden="true">
            <div className="ripple-grid">
                {cells.map((_, index) => {
                    const row = Math.floor(index / GRID_SIZE);
                    const col = index % GRID_SIZE;
                    const activeRow = activeCell === null ? center : Math.floor(activeCell / GRID_SIZE);
                    const activeCol = activeCell === null ? center : activeCell % GRID_SIZE;
                    const distance = Math.abs(row - activeRow) + Math.abs(col - activeCol);

                    return (
                        <span
                            key={index}
                            className="ripple-cell"
                            style={{
                                '--ripple-delay': `${distance * 45}ms`,
                                '--ripple-opacity': Math.max(0.08, 0.42 - distance * 0.045)
                            }}
                            onMouseEnter={() => setActiveCell(index)}
                            onFocus={() => setActiveCell(index)}
                        />
                    );
                })}
            </div>
        </div>
    );
};
