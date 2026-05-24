@negative @login
Feature: Negative login scenarios
  As a user of the Credo Business mobile app
  I want incorrect login attempts to be rejected with localized error messages
  So that I cannot bypass authentication and I understand what went wrong

  Background:
    Given user is on login page


  Scenario Outline: Mandatory-field and invalid-character errors — <language>
    When user changes language to "<language>"
    And user enters username "<username>" and password "<password>"
    And user clears the "<field_to_clear>" field
    Then login button should be in "disabled" state
    And "<error_field>" error message should match key "<error_key>"

    @ka
    Examples:
      | language | username | password | field_to_clear | error_field | error_key                  |
      | ka       | David    |          | username       | username    | error.mandatoryField       |
      | ka       |          | Lynch    | password       | password    | error.mandatoryField       |
      | ka       | #        |          |                | username    | error.invalidUsernameChars |

    @en
    Examples:
      | language | username | password | field_to_clear | error_field | error_key                  |
      | en       | David    |          | username       | username    | error.mandatoryField       |
      | en       |          | Lynch    | password       | password    | error.mandatoryField       |
      | en       | #        |          |                | username    | error.invalidUsernameChars |

    @ru
    Examples:
      | language | username | password | field_to_clear | error_field | error_key                  |
      | ru       | David    |          | username       | username    | error.mandatoryField       |
      | ru       |          | Lynch    | password       | password    | error.mandatoryField       |
      | ru       | #        |          |                | username    | error.invalidUsernameChars |

  Scenario Outline: Wrong credentials surface a localized flash error — <language>
    When user changes language to "<language>"
    And user enters username "<username>" and password "<password>"
    Then login button should be in "enabled" state
    When user clicks login button
    Then flash error should match key "error.authFailed"

    @ka
    Examples:
      | language | username | password |
      | ka       | David    | Lynch    |

    @en
    Examples:
      | language | username | password |
      | en       | David    | Lynch    |

    @ru
    Examples:
      | language | username | password |
      | ru       | David    | Lynch    |
