Feature: Homepage Content and Structure
  As a visitor to the B2C2 website
  I want to see comprehensive company information on the homepage
  So that I can understand B2C2's services and global presence

  Background:
    Given the user is on the B2C2 homepage
    And the user has dismissed the cookie consent

  @smoke
  Scenario: Hero section displays brand messaging
    Then the hero section should display the company tagline
    And the hero section should display the partner description

  @smoke
  Scenario: Navigation contains all main menu categories
    Then the navigation bar should contain the following menus:
      | menuName          |
      | Solutions         |
      | About             |
      | News & Insights   |
      | Join B2C2         |

  @smoke
  Scenario: Solutions dropdown lists all product pages
    Then the "Solutions" dropdown should contain the following items:
      | itemName           |
      | Trading Overview   |
      | OTC Products       |
      | Market Making      |
      | Liquidity Partner  |
      | Stablecoin Swaps   |

  @regression
  Scenario: Solutions dropdown opens on hover, reveals items, and closes
    When the user hovers over the "Solutions" navigation dropdown
    Then the "Solutions" dropdown panel should be visible
    And the visible dropdown items should include "Trading Overview"
    When the user moves away from the "Solutions" navigation dropdown
    Then the "Solutions" dropdown panel should not be visible

  @regression
  Scenario: News slider displays recent press releases with titles and dates
    Then the news slider should display at least 3 articles
    And each news slider article should have a title and date

  @regression
  Scenario: Footer displays all global office locations
    Then the footer should display the following office locations:
      | location                 |
      | London (HQ)              |
      | New Jersey               |
      | Tokyo                    |
      | Luxembourg               |
      | Singapore                |
      | Poland (Technology Hub)  |
      | France                   |

  @regression
  Scenario: Social media links are present and open in new tabs
    Then the footer should contain social media links for:
      | platform  |
      | LinkedIn  |
      | Twitter   |
      | Medium    |
      | YouTube   |
    And all social media links should open in new tabs

  @regression
  Scenario: User can navigate from homepage to contact page via nav bar
    When the user clicks the "CONTACT" button in the navigation bar
    Then the page URL should contain "/contact-us"
    And the page heading should display "Get in touch"

  @smoke
  Scenario: About dropdown lists all company pages
    Then the "About" dropdown should contain the following items:
      | itemName         |
      | About Us         |
      | Meet the Team    |
      | Memberships      |
      | Social Impact    |
      | In the Community |
      | FAQs             |

  @smoke
  Scenario: News and Insights dropdown lists all content pages
    Then the "News & Insights" dropdown should contain the following items:
      | itemName            |
      | B2C2 in the News    |
      | Press Releases      |
      | Events              |
      | Insights            |

  @smoke
  Scenario: Join B2C2 dropdown lists all recruitment pages
    Then the "Join B2C2" dropdown should contain the following items:
      | itemName     |
      | Our Values   |
      | Careers      |

  @smoke
  Scenario: Homepage has correct page title and meta description
    Then the page title should contain "B2C2"
    And the meta description should contain "liquidity provider"

  @smoke
  Scenario: Header logo links to homepage and is visible
    Then the header logo should link to the homepage
    And the header logo image should be visible

  @regression
  Scenario: Footer contains sitemap links for key sections
    Then the footer should contain a sitemap link for "Trading Overview" pointing to "/solutions/trading-overview"
    And the footer should contain a sitemap link for "About Us" pointing to "/about/about-us"
    And the footer should contain a sitemap link for "Careers" pointing to "/join-b2c2/careers"
    And the footer should contain a sitemap link for "FAQs" pointing to "/about/faqs"
