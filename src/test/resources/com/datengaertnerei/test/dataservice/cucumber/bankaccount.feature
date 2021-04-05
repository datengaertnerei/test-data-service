Feature: Verify the bank account api

  Scenario: client makes call to GET bank account
    When the client asks for bank account in Hamburg
    Then the client receives status code OK
    And the client receives bank account in this city
        
  Scenario: client makes call to GET bank account
    When the client asks for bank account in Unbekannt
    Then the client receives status code OK
    