package com.komquests.api.http;

import com.komquests.api.rest.RestService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

public class HttpConnectorTests {
    @Test
    public void testHttpConnectorCanReturnValidHttpRequest() {
        RestService restService = Mockito.mock(RestService.class);
        Mockito.when(restService.get(Mockito.anyString())).thenReturn(getValidResponse());

        HttpConnector httpConnector = new HttpConnector(restService);
        String response = httpConnector.get("https://www.google.com");

        assertNotNull(response);
    }

    @Test
    public void testHttpConnectorCanReturnInvalidHttpRequest() {
        RestService restService = Mockito.mock(RestService.class);
        Mockito.when(restService.get(Mockito.anyString())).thenReturn(null);

        HttpConnector httpConnector = new HttpConnector(restService);
        String response = httpConnector.get("https://www.google.com");

        assertNull(response);
    }

    @Test
    public void testHttpConnectorCanReturnTrimmedDataFromValidHttpRequest() {
        RestService restService = Mockito.mock(RestService.class);
        Mockito.when(restService.get(Mockito.anyString())).thenReturn(getValidResponse());

        HttpConnector httpConnector = new HttpConnector(restService);
        String trimStart = "fixed-bottom bg-light text-dark";
        String trimEnd = "/www.linkedin.com/in/nab";
        String response = httpConnector.get("https://www.google.com", trimStart, trimEnd);

        String expectedResponse = " footer\">\n" +
                "    <center>\n" +
                "      <h6><small><a href=\"https://www.brentryczak.com\" target=\"_top\">Brent Ryczak</a> · <a href=\"https:/";
        assertEquals(expectedResponse, response);
    }

    @Test
    public void testHttpConnectorCanReturnTrimmedDataFromInvalidHttpRequest() {
        RestService restService = Mockito.mock(RestService.class);
        Mockito.when(restService.get(Mockito.anyString())).thenReturn(null);

        HttpConnector httpConnector = new HttpConnector(restService);
        String trimStart = "fixed-bottom bg-light text-dark";
        String trimEnd = "/www.linkedin.com/in/nab";
        String response = httpConnector.get("https://www.google.com", trimStart, trimEnd);

        assertNull(response);
    }

    public String getValidResponse() {
        return "<html>\n" +
                "  <head>\n" +
                "    <title>Know Your Cosmos</title>\n" +
                "\n" +
                "    <link rel=\"stylesheet\" href=\"https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css\" integrity=\"sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T\" crossorigin=\"anonymous\">\n" +
                "    <link rel='shortcut icon' type='image/x-icon' href='https://www.knowyourcosmos.com/favicon.ico'/>\n" +
                "\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
                "\n" +
                "    <!-- Global site tag (gtag.js) - Google Analytics -->\n" +
                "    <script async src=\"https://www.googletagmanager.com/gtag/js?id=G-E9RX775H6Y\"></script>\n" +
                "    <script>\n" +
                "      window.dataLayer = window.dataLayer || [];\n" +
                "      function gtag(){dataLayer.push(arguments);}\n" +
                "      gtag('js', new Date());\n" +
                "\n" +
                "      gtag('config', 'G-E9RX775H6Y');\n" +
                "    </script>\n" +
                "    \n" +
                "    <style>\n" +
                "      body { \n" +
                "        background-image: url('./resources/logo_10.png');\n" +
                "        background-size: 60%;\n" +
                "        background-repeat: no-repeat;\n" +
                "        background-attachment: fixed;\n" +
                "        background-position: center;\n" +
                "        padding-bottom: 70px;\n" +
                "      }\n" +
                "      .footer {\n" +
                "        padding-top: 13px;\n" +
                "        padding-bottom: 5px;\n" +
                "      }\n" +
                "      .jumbotron {\n" +
                "        background: none\n" +
                "      }\n" +
                "      .right{\n" +
                "          float:right;\n" +
                "      }\n" +
                "      .left{\n" +
                "          float:left;\n" +
                "      }\n" +
                "      #wrapper{\n" +
                "          text-align: center;\n" +
                "      }\n" +
                "    </style>\n" +
                "  </head>\n" +
                "\n" +
                "  <body>\n" +
                "    <div class=\"jumbotron\">\n" +
                "      <center>\n" +
                "        <h1>Know Your Cosmos</h1>\n" +
                "      </center>\n" +
                "      <hr class=\"my-4\">\n" +
                "\n" +
                "      <div><center><span class=\"badge badge-dark\">server status</span><span class=\"badge badge-success\">live</span> <span class=\"badge badge-dark\">version</span><span class=\"badge badge-info\">1.5.0</span></center><br></div>\n" +
                "      <center>\n" +
                "        <p class=\"lead\"><a href=\"https://apps.apple.com/us/app/know-your-cosmos/id1451492400\"><img style=\"size: 40%\" src=\"./resources/download.png\" alt=\"Download Know Your Cosmos\"/></a></p>\n" +
                "        <hr class=\"my-4\">\n" +
                "      </center>\n" +
                "\n" +
                "      <center><div><br>Cosmos Quiz Leaderboard<hr><table style=\"width: auto;\" class=\"table table-bordered table-sm\"><thead><tr><th scope='col'>Place</th><th scope='col'>Username</th><th scope='col'>Points</th></tr></thead><tbody><tr><td><center><font size='2'>1</font></center></td><td><font size='2'>Clo</font></td><td><center><font size='2'>1110</font></center></td></tr><tr><td><center><font size='2'>2</font></center></td><td><font size='2'>Clo</font></td><td><center><font size='2'>800</font></center></td></tr><tr><td><center><font size='2'>3</font></center></td><td><font size='2'>BDC</font></td><td><center><font size='2'>720</font></center></td></tr><tr><td><center><font size='2'>4</font></center></td><td><font size='2'>Clo</font></td><td><center><font size='2'>660</font></center></td></tr><tr><td><center><font size='2'>5</font></center></td><td><font size='2'>BDC</font></td><td><center><font size='2'>660</font></center></td></tr><tr><td><center><font size='2'>6</font></center></td><td><font size='2'>Clo</font></td><td><center><font size='2'>520</font></center></td></tr><tr><td><center><font size='2'>7</font></center></td><td><font size='2'>LearnedQuality</font></td><td><center><font size='2'>490</font></center></td></tr><tr><td><center><font size='2'>8</font></center></td><td><font size='2'>BDC</font></td><td><center><font size='2'>480</font></center></td></tr><tr><td><center><font size='2'>9</font></center></td><td><font size='2'>None</font></td><td><center><font size='2'>430</font></center></td></tr><tr><td><center><font size='2'>10</font></center></td><td><font size='2'>WealthyDesk</font></td><td><center><font size='2'>420</font></center></td></tr></tbody></table><hr></div></center>\n" +
                "      <center><div><br>App Usage<hr><center><font size='2'>Statistics</font></center><table style=\"width: auto;\" class=\"table table-bordered table-sm\"><tbody><tr><td><font size='2'>Total Users</font></td><td><center><font size='2'>916</font></center></td></tr><tr><td><font size='2'>Total Games Plays</font></td><td><center><font size='2'>3,989</font></center></td></tr><tr><td><font size='2'>Total Chats Sent</font></td><td><center><font size='2'>123</font></center></td></tr><tr><td><font size='2'>Total Times Correct</font></td><td><center><font size='2'>7,427</font></center></td></tr><tr><td><font size='2'>Total Times Incorrect</font></td><td><center><font size='2'>4,427</font></center></td></tr><tr><td><font size='2'>Correct Answer Average</font></td><td><center><font size='2'>59.607%</font></center></td></tr></tbody></table><table style=\"width: auto;\" class=\"table table-bordered table-sm\"><center><font size='2'>Cosmos Quiz Usage</font></center><tbody><tr><td><font size='2'>Most Dedicated Player</font></td><td><font size='2'>crunchypringle (443 plays) </font></td></tr><tr><td><font size='2'>Most Frequent Player</font></td><td><font size='2'>crunchypringle (9 days) </font></td></tr><tr><td><font size='2'>Most Recent Player</font></td><td><font size='2'>LowAgreement (847 minutes ago) </font></td></tr></tbody></table><table style=\"width: auto;\" class=\"table table-bordered table-sm\"><center><font size='2'>Cosmic Chat Usage</font></center><tbody><tr><td><font size='2'>Most Active Chatter</font></td><td><font size='2'>NotCrunchyPringle (25 chats)</font></td></tr><tr><td><font size='2'>Most Recent Chatter </font></td><td><font size='2'>Lord Beerent (2,327 minutes ago)</font></td></tr><tr><td><font size='2'>Most Recent Chat Visitor</font></td><td><font size='2'>Lord Beerent (2,168 minutes ago)</font></td></tr></tbody></table><hr></div></center>\n" +
                "      <br><center>Release Notes</center><hr><div class='overflow-auto' style='width:85%; height:150px; margin:auto;' id='release_notes_wrapper'> <p style='white-space: pre-wrap;'><hr style='height:2px;border-width:0; color:gray; background-color:gray'><p><span class=\"left\"><font size='1'><b>version 1.5.0</b></font></span><span class=\"right\"><font size='1'>2021-05-13</font></span></p><br><p><font size='2'>updates:<br>- retired Cosmos Live<br>- added Cosmic Chat<br>- added system and admin users in Cosmic Chat</font></p><hr style='height:2px; border-width:0; color:gray; background-color:gray'></p><p style='white-space: pre-wrap;'><p><span class=\"left\"><font size='1'><b>version 1.4.0</b></font></span><span class=\"right\"><font size='1'>2021-02-22</font></span></p><br><p><font size='2'>updates:<br>- added 3 lives to Challenge Mode<br>- display correct/ incorrect message after answer selection in Challenge Mode<br>- display correct answer when correct answer is selected in Challenge Mode<br>- added 3-second timer after answer is selected in Challenge Mode<br>- added Cosmos Live game mode (pre-game lobby only)<br>- added credits easter egg<br><br>bug fixes:<br>- fixed lagging UI when loading Challenge Mode leaderboard<br>- fixed lingering loading message</font></p><hr style='height:2px; border-width:0; color:gray; background-color:gray'></p><p style='white-space: pre-wrap;'><p><span class=\"left\"><font size='1'><b>version 1.3.0</b></font></span><span class=\"right\"><font size='1'>2020-12-16</font></span></p><br><p><font size='2'>updates:<br>  - Added Homepage Notification/ Message Bar<br><br>bug fixes:<br>  - resolved UI issues with user selection screen<br>  - resolved bug allowing empty username on user selection screen<br>  - cleaned challenge mode timer display</font></p><hr style='height:2px; border-width:0; color:gray; background-color:gray'></p><p style='white-space: pre-wrap;'><p><span class=\"left\"><font size='1'><b>version 1.2.0</b></font></span><span class=\"right\"><font size='1'>2020-12-09</font></span></p><br><p><font size='2'>minor update contains the following updates:<br>  - moved username selection to its own dialog<br>  - added ability to remember username<br>  - resolved UI bug during loading causing white flash<br>  - corrected the planets orbit directions<br>  - added app version on home screen</font></p><hr style='height:2px; border-width:0; color:gray; background-color:gray'></p><p style='white-space: pre-wrap;'><p><span class=\"left\"><font size='1'><b>version 1.1.1</b></font></span><span class=\"right\"><font size='1'>2020-12-03</font></span></p><br><p><font size='2'>patch update with the following improvements:<br>  - fixed bug with UI displaying multiple timers<br>  - fixed bug with loading string inconsistently hanging around<br>  - other minor improvements</font></p><hr style='height:2px; border-width:0; color:gray; background-color:gray'></p><p style='white-space: pre-wrap;'><p><span class=\"left\"><font size='1'><b>version 1.1.0</b></font></span><span class=\"right\"><font size='1'>2020-12-02</font></span></p><br><p><font size='2'>- Added a timer for questions to spice up the challenge<br>- Changed app icon<br>- Added a launch screen<br>- Minor bug improvements</font></p><hr style='height:2px; border-width:0; color:gray; background-color:gray'></p><p style='white-space: pre-wrap;'><p><span class=\"left\"><font size='1'><b>version 1.0.3</b></font></span><span class=\"right\"><font size='1'>2020-11-07</font></span></p><br><p><font size='2'>- Fixed bug with using space in username.<br>- Fixed bug with question text on iPad.</font></p><hr style='height:2px; border-width:0; color:gray; background-color:gray'></p></div><hr>      <div>\n" +
                "      <center>\n" +
                "        <a href=\"https://github.com/beerent/cosmos_server\">Server Github</a> · <a href=\"https://github.com/beerent/cosmos_client\">Client Github</a>\n" +
                "      </center>\n" +
                "      </div>\n" +
                "    </div>\n" +
                "\n" +
                "    <div class=\"fixed-bottom bg-light text-dark footer\">\n" +
                "    <center>\n" +
                "      <h6><small><a href=\"https://www.brentryczak.com\" target=\"_top\">Brent Ryczak</a> · <a href=\"https://www.linkedin.com/in/nabarletta/\" target=\"_top\">Nick Barletta</a></small></h6>\n" +
                "    </center>\n" +
                "    </div>\n" +
                "  </body>\n" +
                "</html>\n";
    }
}
