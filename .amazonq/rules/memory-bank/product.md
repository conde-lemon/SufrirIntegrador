# Travel4U - Product Overview

## Purpose
Travel4U is a comprehensive web-based travel reservation platform that centralizes the search and booking of travel services. The system integrates real-time flight offer scraping, user authentication, reservation management, and payment processing to provide a complete travel booking experience.

## Value Proposition
- **Centralized Travel Services**: Single platform for flights, cruises, and bus services
- **Real-Time Offers**: Web scraping integration with Skyscanner for live flight deals
- **Secure Booking**: Complete reservation workflow with PayPal payment integration
- **User Management**: Role-based access control with admin and client roles
- **Report Generation**: PDF reports for reservations, users, and promotions using JasperReports

## Key Features

### User Management
- User registration and authentication with Spring Security
- Role-based access (ADMIN, USER)
- Default admin account creation on first startup (admin@travel4u.com)
- User profile management

### Service Catalog
- **Flights**: Domestic and international flight services
- **Cruises**: Multi-day cruise packages
- **Buses**: Intercity bus transportation
- Search and filter by origin, destination, and service type
- Provider management for airlines, cruise lines, and bus companies

### Reservation System
- Seat selection interface for flights
- Additional baggage selection
- Multi-step booking process (service → seat → payment → confirmation)
- Reservation history and management
- Unique reservation codes (TFU-YYYY-XXXX format)

### Payment Processing
- PayPal payment gateway integration
- Simulated payment processing with animations
- Payment confirmation with reference numbers
- Payment history tracking

### Reporting & Analytics
- PDF report generation for reservations
- User management reports
- Promotion/offer reports
- Admin dashboard with Power BI integration

### Web Scraping & External APIs
- Skyscanner flight offer extraction
- Amadeus API integration (test environment)
- Automated offer data extraction and storage

## Target Users

### Travelers (Clients)
- Search and compare travel services
- Make reservations with seat selection
- Process payments securely
- View and manage booking history
- Download booking receipts

### Administrators
- Manage users and providers
- Create and manage services (flights, cruises, buses)
- Create promotional offers
- Generate business reports
- Monitor system activity via dashboard

## Use Cases

1. **Flight Booking**: User searches Lima to Madrid flights, selects seat, adds baggage, pays via PayPal, receives confirmation
2. **Cruise Reservation**: User browses cruise packages, books Mediterranean cruise, completes payment
3. **Admin Service Management**: Admin adds new flight routes, creates promotional offers, generates user reports
4. **Offer Scraping**: System automatically extracts flight deals from Skyscanner and displays on homepage
5. **Report Generation**: Admin generates PDF reports for reservations by user or date range
