# Konfuzio PDF Tools - Customization Documentation

## Overview
This document details all customizations made to transform Stirling-PDF v1.2.0 into Konfuzio PDF Tools, operated by Helm & Nagel GmbH.

**Base Version:** Stirling-PDF v1.2.0  
**Customized Version:** Konfuzio PDF Tools  
**Operator:** Helm & Nagel GmbH  
**Date:** August 11, 2024

---

## 🎯 **Branding Changes**

### Application Identity
- **Application Name:** Changed from "Stirling-PDF" to "Konfuzio PDF Tools"
- **Navbar Brand:** Changed from "Stirling-PDF" to "Konfuzio"
- **Home Description:** Changed to "PDF Tools from Konfuzio"
- **Meta Tags:** Updated all meta descriptions and titles to reflect Konfuzio branding

### Visual Branding
- **Favicon:** Replaced all favicon files (`.ico`, `.png`, `.svg`, `.icns`) with Konfuzio logo
- **Apple Touch Icon:** Updated to Konfuzio logo
- **Safari Pinned Tab:** Updated to Konfuzio logo
- **Cache Busting:** Added `?v=konfuzio` parameter to favicon links to ensure new logo loads

---

## 🏢 **Legal & Contact Information**

### Company Details
- **Operator:** Helm & Nagel GmbH
- **Address:** Rosenweg 5, 35614 Aßlar, Deutschland
- **Email:** info@helm-nagel.com

### Legal Pages Created
1. **Privacy Policy** (`/privacy-policy`)
   - GDPR-compliant privacy policy
   - Helm & Nagel GmbH contact information
   - Data processing and retention policies
   - User rights and cookie usage

2. **Terms & Conditions** (`/terms`)
   - Complete terms of service
   - Service description and user responsibilities
   - Intellectual property (mentions Stirling-PDF MIT license)
   - Governing law (Germany)
   - Helm & Nagel GmbH contact information

### Footer Integration
- **Privacy Policy Link:** Points to `/privacy-policy`
- **Terms & Conditions Link:** Points to `/terms`
- **Powered By:** Changed from "Stirling PDF" to "Konfuzio"
- **Removed Links:** "Releases" and "Survey" links removed

---

## 🧹 **Removed Features & Links**

### External Stirling Links Removed
- **Documentation Links:**
  - Pipeline documentation (`docs.stirlingpdf.com`)
  - OCR documentation (`docs.stirlingpdf.com`)
- **Survey & Analytics:**
  - Survey links (`stirlingpdf.info`)
  - Calendly meeting links (`calendly.com/stirling-pdf`)
  - Analytics pixel (`pixel.stirlingpdf.com`)
- **External Services:**
  - Docker Hub link (points to Stirling's Docker Hub)
  - Error page GitHub link (updated to user's fork)

### UI Elements Removed
- **"Go Pro" Badge:** Removed from navbar
- **Settings Icon:** Removed from top-right corner
- **Analytics Modal:** Removed from home pages
- **Update Notifications:** Removed update links

### Survey & Analytics Content
- **Survey Modal:** Completely removed from `home.html` and `home-legacy.html`
- **Analytics JavaScript:** Disabled PostHog analytics pixel
- **Cookie Consent:** Updated to mention Konfuzio instead of Stirling PDF

---

## 🔧 **Technical Changes**

### Build Configuration
- **Modular Structure:** Integrated Stirling-PDF v1.2.0 modular architecture
- **Proprietary Module:** Removed `app/proprietary/` module and all references
- **Gradle Files:** Updated `settings.gradle`, `build.gradle`, and `app/core/build.gradle`
- **Enterprise Features:** Removed all enterprise/EE functionality

### Controller Updates
- **HomeWebController:** Added endpoints for `/privacy-policy` and `/terms`
- **Legal Pages:** Created new controller methods for legal pages

### Settings Configuration
- **Legal URLs:** Updated to point to local pages instead of external URLs
- **Analytics:** Disabled by default (`enableAnalytics: false`)
- **Branding:** Set Konfuzio branding in `settings.yml`

---

## 🌐 **Internationalization Updates**

### Cookie Messages Updated
- **English (US):** Updated cookie consent messages
- **English (GB):** Updated cookie consent messages  
- **German (DE):** Updated all cookie-related messages including:
  - Popup description
  - Preferences modal descriptions
  - Analytics descriptions

### Translation Keys
- **Privacy Policy:** Uses existing `legal.privacy` translation key
- **Terms & Conditions:** Uses existing `legal.terms` translation key
- **Footer Links:** Uses existing translation system

---

## 📝 **Content Updates**

### User Interface Text
- **Watermark Placeholders:** Changed from "Stirling-PDF" to "Konfuzio"
- **Login Page Title:** Updated to "Konfuzio PDF Tools"
- **Cookie Messages:** Updated to mention "Konfuzio PDF Tools"

### Third-Party Licenses
- **Added Stirling-PDF:** Acknowledged as open-source foundation in `3rdPartyLicenses.json`
- **License Compliance:** Maintained proper attribution to GPL-3.0 license

---

## 🚀 **Deployment Configuration**

### Server Settings
- **Port:** 8080 (default)
- **Hot Reloading:** Enabled via Spring Boot DevTools
- **Analytics:** Disabled by default
- **Cookie Consent:** Configured for Konfuzio branding

### File Structure
```
app/
├── core/                    # Main application
│   ├── src/main/resources/
│   │   ├── templates/       # HTML templates
│   │   ├── static/         # Static assets (favicons, CSS, JS)
│   │   └── messages_*.properties  # Internationalization
│   └── configs/settings.yml # Application configuration
└── common/                  # Shared modules
```

---

## 📋 **Files Modified**

### HTML Templates
- `fragments/common.html` - Updated favicon links and analytics
- `fragments/navbar.html` - Removed "Go Pro" badge and settings icon
- `fragments/footer.html` - Updated links and "Powered by" text
- `home.html` - Removed analytics modal and survey links
- `home-legacy.html` - Removed analytics modal and survey links
- `login.html` - Updated title
- `security/add-watermark.html` - Updated placeholder
- `security/remove-watermark.html` - Updated placeholder
- `pipeline.html` - Removed documentation links
- `misc/ocr-pdf.html` - Removed documentation link
- `error.html` - Updated GitHub link
- `privacy-policy.html` - **NEW** - Complete privacy policy
- `terms.html` - **NEW** - Complete terms and conditions

### Configuration Files
- `app/core/configs/settings.yml` - Updated branding and legal URLs
- `app/core/build.gradle` - Removed proprietary module references
- `settings.gradle` - Removed proprietary module
- `build.gradle` - Removed proprietary module references

### Java Controllers
- `HomeWebController.java` - Added legal page endpoints

### Properties Files
- `messages_en_US.properties` - Updated cookie messages
- `messages_en_GB.properties` - Updated cookie messages
- `messages_de_DE.properties` - Updated cookie messages

### Static Assets
- All favicon files replaced with Konfuzio logo:
  - `favicon.ico`
  - `favicon.png`
  - `favicon.svg`
  - `favicon.icns`
  - `apple-touch-icon.png`
  - `safari-pinned-tab.svg`

---

## 🔒 **License Compliance**

### Open Source Attribution
- **Base Project:** Stirling-PDF v1.2.0 (MIT License)
- **Modifications:** Helm & Nagel GmbH
- **License Text:** Available at `/terms` page
- **Third-Party Licenses:** Acknowledged in `/licenses` page

### MIT License Compliance
- **Source Code:** Available in Git repository
- **Modifications:** Documented in this file
- **Attribution:** Properly credited in terms and licenses pages
- **Original License:** Preserved in LICENSE file

---

## 🌍 **Access URLs**

### Main Application
- **Homepage:** http://localhost:8080
- **Privacy Policy:** http://localhost:8080/privacy-policy
- **Terms & Conditions:** http://localhost:8080/terms
- **Licenses:** http://localhost:8080/licenses

### Footer Links
- **Privacy Policy:** `/privacy-policy`
- **Terms & Conditions:** `/terms`
- **Licenses:** `/licenses`
- **Powered By:** https://konfuzio.com

---

## ✅ **Verification Checklist**

### Branding
- [x] Application name shows "Konfuzio PDF Tools"
- [x] Navbar shows "Konfuzio"
- [x] Favicon displays Konfuzio logo
- [x] All meta tags updated
- [x] Cookie messages mention Konfuzio

### Legal Pages
- [x] Privacy Policy accessible at `/privacy-policy`
- [x] Terms & Conditions accessible at `/terms`
- [x] Helm & Nagel GmbH contact information included
- [x] Footer links working correctly

### Removed Features
- [x] No "Go Pro" button visible
- [x] No settings icon in navbar
- [x] No analytics modal on homepage
- [x] No survey links
- [x] No external Stirling documentation links

### Functionality
- [x] Application starts successfully
- [x] All PDF tools working
- [x] Hot reloading enabled
- [x] No enterprise features present

---

## 📞 **Support & Contact**

For questions about this customization or technical support:

**Helm & Nagel GmbH**  
Rosenweg 5  
35614 Aßlar  
Deutschland  

**Email:** info@helm-nagel.com

---

*This document was generated on August 11, 2024, and reflects the current state of Konfuzio PDF Tools customization.*
