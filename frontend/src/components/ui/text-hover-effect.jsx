import React, { useId, useState } from 'react';

export const TextHoverEffect = ({ text, className = '' }) => {
    const maskId = useId().replace(/:/g, '');
    const [position, setPosition] = useState({ x: 50, y: 50 });
    const [isActive, setIsActive] = useState(false);

    const handlePointerMove = (event) => {
        const bounds = event.currentTarget.getBoundingClientRect();
        const x = ((event.clientX - bounds.left) / bounds.width) * 100;
        const y = ((event.clientY - bounds.top) / bounds.height) * 100;
        setPosition({ x, y });
    };

    return (
        <div
            className={`text-hover-effect ${className}`}
            onPointerMove={handlePointerMove}
            onPointerEnter={() => setIsActive(true)}
            onPointerLeave={() => setIsActive(false)}
        >
            <svg viewBox="0 0 1200 300" role="img" aria-label={text}>
                <defs>
                    <radialGradient id={`${maskId}-gradient`} cx={`${position.x}%`} cy={`${position.y}%`} r={isActive ? '23%' : '0%'}>
                        <stop offset="0%" stopColor="white" />
                        <stop offset="55%" stopColor="white" />
                        <stop offset="100%" stopColor="black" />
                    </radialGradient>
                    <mask id={maskId}>
                        <rect width="100%" height="100%" fill={`url(#${maskId}-gradient)`} />
                    </mask>
                </defs>

                <text x="50%" y="50%" textAnchor="middle" dominantBaseline="middle" className="text-hover-outline">
                    {text}
                </text>
                <text x="50%" y="50%" textAnchor="middle" dominantBaseline="middle" className="text-hover-fill" mask={`url(#${maskId})`}>
                    {text}
                </text>
            </svg>
        </div>
    );
};
