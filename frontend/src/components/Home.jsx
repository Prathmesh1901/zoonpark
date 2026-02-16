import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import { motion, AnimatePresence } from 'framer-motion';
import {
    FiSearch, FiCalendar, FiCreditCard, FiMapPin, FiArrowRight,
    FiPercent, FiHelpCircle, FiChevronDown, FiChevronUp, FiInstagram, FiTwitter, FiFacebook, FiLinkedin
} from 'react-icons/fi';
import logo from '../assets/logg.png';

const Home = () => {
    const containerVariants = {
        hidden: { opacity: 0 },
        visible: {
            opacity: 1,
            transition: { staggerChildren: 0.2 }
        }
    };

    const itemVariants = {
        hidden: { opacity: 0, y: 20 },
        visible: { opacity: 1, y: 0, transition: { duration: 0.5 } }
    };

    return (
        <motion.div
            className="home-container"
            initial="hidden"
            animate="visible"
            variants={containerVariants}
        >
            {/* Hero Section */}
            <motion.div className="hero-section" variants={itemVariants}>
                <motion.h1 className="hero-title">
                    Parking Made <span style={{ color: 'var(--primary)' }}>Simple</span>
                </motion.h1>
                <motion.p className="hero-subtitle">
                    Find the perfect spot in seconds. Book ahead, park stress-free,
                    and explore the city without limits.
                </motion.p>
                <motion.div className="hero-buttons">
                    <Link to="/search" className="btn btn-primary btn-large">
                        <FiSearch /> Find Parking
                    </Link>
                    <Link to="/register" className="btn btn-secondary btn-large">
                        Get Started <FiArrowRight />
                    </Link>
                </motion.div>
            </motion.div>

            {/* Trending Offers */}
            <div className="section-container" style={{ padding: '0 2rem' }}>
                <motion.h2 variants={itemVariants} className="section-title">Trending Offers</motion.h2>
                <motion.div className="offers-grid" variants={containerVariants}>
                    <div className="offer-card">
                        <div className="offer-icon"><FiPercent /></div>
                        <div className="offer-content">
                            <h3>First Booking Special</h3>
                            <p>Get flat 25% off on your first parking reservation.</p>
                            <span className="offer-code">WELCOME25</span>
                        </div>
                    </div>
                    <div className="offer-card">
                        <div className="offer-icon"><FiCalendar /></div>
                        <div className="offer-content">
                            <h3>Weekend Pass</h3>
                            <p>Unlimited parking on weekends at selected spots.</p>
                            <span className="offer-code">WEEKEND</span>
                        </div>
                    </div>
                    <div className="offer-card">
                        <div className="offer-icon"><FiCreditCard /></div>
                        <div className="offer-content">
                            <h3>Monthly Saver</h3>
                            <p>Save up to 40% with monthly subscriptions.</p>
                            <span className="offer-code">MONTHLY40</span>
                        </div>
                    </div>
                </motion.div>
            </div>

            {/* Features Section */}
            <div className="features-section">
                <motion.h2 variants={itemVariants} className="section-title">Why Choose ZoomPark?</motion.h2>
                <motion.div className="features-grid" variants={containerVariants}>
                    <FeatureCard icon={<FiSearch />} title="Smart Search" desc="Find available spaces nearby instantly with real-time availability." />
                    <FeatureCard icon={<FiCalendar />} title="Easy Booking" desc="Reserve your spot in advance. No more circling the block." />
                    <FeatureCard icon={<FiCreditCard />} title="Secure Payment" desc="Transparent pricing with no hidden fees. Pay securely online." />
                    <FeatureCard icon={<FiMapPin />} title="Prime Locations" desc="Access exclusive parking spots in high-demand areas." />
                </motion.div>
            </div>

            {/* Top Locations */}
            <div className="section-container" style={{ padding: '0 2rem' }}>
                <motion.h2 variants={itemVariants} className="section-title">Zoom Around India</motion.h2>
                <motion.div className="cities-grid" variants={containerVariants}>
                    {['Bangalore', 'Mumbai', 'Delhi', 'Pune', 'Hyderabad', 'Chennai'].map(city => (
                        <div key={city} className="city-card">
                            <img
                                src={`https://placehold.co/600x400/1e293b/FFF?text=${city}`}
                                alt={city}
                                className="city-bg"
                            />
                            <div className="city-overlay">
                                <div className="city-name">{city}</div>
                            </div>
                        </div>
                    ))}
                </motion.div>
            </div>

            {/* FAQ Section */}
            <div className="faq-section">
                <motion.h2 variants={itemVariants} className="section-title">Frequently Asked Questions</motion.h2>
                <FAQItem question="How do I book a parking spot?" answer="Simply search for your location, choose a spot, select your time, and pay securely online. You'll receive a digital pass instantly." />
                <FAQItem question="Can I cancel my booking?" answer="Yes! You can cancel up to 1 hour before your booking time for a full refund." />
                <FAQItem question="Is my vehicle safe?" answer="We verify all our parking hosts. Many locations also offer CCTV surveillance and security guards." />
                <FAQItem question="How do I list my parking space?" answer="Click on 'Become a Host', register your details, upload photos of your spot, and start earning effortlessly." />
            </div>

            {/* CTA Section */}
            <motion.div className="cta-section text-center" variants={itemVariants}>
                <h2>Have an empty parking space?</h2>
                <p style={{ marginBottom: '2rem', color: 'var(--text-secondary)' }}>
                    Turn your unused driveway or garage into passive income.
                    Join thousands of owners earning today.
                </p>
                <Link to="/register" className="btn btn-primary btn-large">
                    Become a Host
                </Link>
            </motion.div>

            {/* Footer */}
            <footer className="footer">
                <div className="footer-grid">
                    <div className="footer-col">
                        <img
                            src={logo}
                            alt="ZoomPark"
                            style={{
                                height: '40px',
                                marginBottom: '1.5rem',
                                background: 'white',
                                padding: '4px',
                                borderRadius: '8px'
                            }}
                        />
                        <p style={{ color: 'var(--text-secondary)', maxWidth: '300px' }}>
                            Founded in 2024, ZoomPark is India's leading parking marketplace connecting drivers with secure parking spots.
                        </p>
                    </div>
                    <div className="footer-col">
                        <h4>Company</h4>
                        <ul className="footer-links">
                            <li><Link to="#">About Us</Link></li>
                            <li><Link to="#">Careers</Link></li>
                            <li><Link to="#">Blog</Link></li>
                            <li><Link to="#">Press</Link></li>
                        </ul>
                    </div>
                    <div className="footer-col">
                        <h4>Support</h4>
                        <ul className="footer-links">
                            <li><Link to="#">Help Center</Link></li>
                            <li><Link to="#">Terms of Service</Link></li>
                            <li><Link to="#">Privacy Policy</Link></li>
                            <li><Link to="#">Contact Us</Link></li>
                        </ul>
                    </div>
                    <div className="footer-col">
                        <h4>Follow Us</h4>
                        <div style={{ display: 'flex', gap: '1rem', fontSize: '1.5rem', color: 'var(--text-secondary)' }}>
                            <FiInstagram style={{ cursor: 'pointer' }} />
                            <FiTwitter style={{ cursor: 'pointer' }} />
                            <FiFacebook style={{ cursor: 'pointer' }} />
                            <FiLinkedin style={{ cursor: 'pointer' }} />
                        </div>
                    </div>
                </div>
                <div className="footer-bottom">
                    <p>&copy; 2026 ZoomPark India Pvt Ltd. All rights reserved.</p>
                </div>
            </footer>
        </motion.div>
    );
};

const FeatureCard = ({ icon, title, desc }) => {
    return (
        <motion.div
            className="feature-card"
            whileHover={{ y: -5, transition: { duration: 0.2 } }}
        >
            <div className="feature-icon" style={{ color: 'var(--primary)', fontSize: '2.5rem', marginBottom: '1rem' }}>
                {icon}
            </div>
            <h3>{title}</h3>
            <p style={{ color: 'var(--text-secondary)' }}>{desc}</p>
        </motion.div>
    );
};

const FAQItem = ({ question, answer }) => {
    const [isOpen, setIsOpen] = useState(false);

    return (
        <div className="faq-item">
            <div className="faq-question" onClick={() => setIsOpen(!isOpen)}>
                {question}
                {isOpen ? <FiChevronUp /> : <FiChevronDown />}
            </div>
            <div className={`faq-answer ${isOpen ? 'open' : ''}`}>
                {answer}
            </div>
        </div>
    );
};

export default Home;
