# ipdsbox [![Build Status](https://travis-ci.org/michaelknigge/ipdsbox.svg?branch=master)](https://travis-ci.org/michaelknigge/ipdsbox) [![codecov.io](https://codecov.io/github/michaelknigge/ipdsbox/coverage.svg?branch=master)](https://codecov.io/github/michaelknigge/ipdsbox?branch=master) [![Coverity Status](https://scan.coverity.com/projects/8130/badge.svg)](https://scan.coverity.com/projects/8130)

**Java library for parsing IPDS data streams. The library is currently in an early design phase and may not be useful for anything.**

This project provides a framework that allows you to implement a virtual IPDS printer. Each received IPDS command is transformed into a Java object. Acknowledge replies can be constructed using Java objects, transformed into IPDS data stream and than sent back to the server.

# PPD/PPR protocol
LAN attached IPDS printers are using an special protocol named *"PPD/PPR"* (Page Printer Daemon / Page Printer Requester). This protocol encapsulates the native IPDS data stream for printers that are attached by TCP/IP (and not SNA).

The specification of this protocol is (sadly) not publicy available. The specification is licenced by [Ricoh](https://www.ricoh-usa.com/) to all members of the [AFP Consortium](http://afpcinc.org/). As the developer of ipdsbox is no member, the implementation of the PPD/PPR protocol in ipdsbox is based on tracing and inspecting the data stream (between a z/OS Host and an IPDS enabled printer).

**Beware: Because of that it is likely that the behaviour of ipdsbox is not correct in all cases.**


# IPDS Command Sets
The IPDS architecture contains several command sets. The following table shows the command sets and the
current status of the corresponding support of the IPDS command (*"supported"* means that ipdsbox can parse
the IPDS command and create a specific Java object for it).

IPDS Command | Hex Value | Command Description                    | Command Set      | Supported
-------------|-----------|----------------------------------------|------------------|----------
AR           | X'D62E'   | Activate Resource                      | Device Control   | :x: 
AFO          | X'D602'   | Apply Finishing Operations             | Device Control   | :x: 
BP           | X'D6AF'   | Begin Page                             | Device Control   | :x: 
DF           | X'D64F'   | Deactivate Font                        | Device Control   | :x: 
DUA          | X'D6CE'   | Define User Area                       | Device Control   | :x: 
END          | X'D65D'   | End                                    | Device Control   | :x: 
EP           | X'D6BF'   | End Page                               | Device Control   | :x: 
ISP          | X'D67E'   | Include Saved Page                     | Device Control   | :x: 
ICMR         | X'D66B'   | Invoke CMR                             | Device Control   | :x: 
LCC          | X'D69F'   | Load Copy Control                      | Device Control   | :x: 
LFE          | X'D63F'   | Load Font Equivalence                  | Device Control   | :x: 
LPD          | X'D6CF'   | Logical Page Descriptor                | Device Control   | :x: 
LPP          | X'D66D'   | Logical Page Position                  | Device Control   | :x: 
MID          | X'D601'   | Manage IPDS Dialog                     | Device Control   | :x: 
NOP          | X'D603'   | No Operation                           | Device Control   | :white_check_mark:
PFC          | X'D634'   | Presentation Fidelity Control          | Device Control   | :x: 
RPO          | X'D67B'   | Rasterize Presentation Object          | Device Control   | :x: 
STM          | X'D6E4'   | Sense Type and Model                   | Device Control   | :white_check_mark:
SHS          | X'D697'   | Set Home State                         | Device Control   | :white_check_mark:
SPE          | X'D608'   | Set Presentation Environment           | Device Control   | :x: 
XOA          | X'D633'   | Execute Order Anystate                 | Device Control   | :x: 
XOH          | X'D68F'   | Execute Order Home State               | Device Control   | :x: 
LE           | X'D61D'   | Load Equivalence                       | Text             | :x:
WTC          | X'D688'   | Write Text Control                     | Text             | :x:
WT           | X'D62D'   | Write Text                             | Text             | :x:
WIC          | X'D63D'   | Write Image Control                    | IM-Image         | :x:
WI           | X'D64D'   | Write Image                            | IM-Image         | :x:
WIC2         | X'D63E'   | Write Image Control 2                  | IO-Image         | :x:
WI2          | X'D64E'   | Write Image 2                          | IO-Image         | :x:
WGC          | X'D684'   | Write Graphics Control                 | Graphics         | :x:
WG           | X'D685'   | Write Graphics                         | Graphics         | :x:
WBCC         | X'D680'   | Write Bar Code Control                 | Bar Code         | :x:
WBC          | X'D681'   | Write Bar Code                         | Bar Code         | :x:
DORE         | X'D66C'   | Data Object Resource Equivalence       | Object Container | :x:
DDOFC        | X'D65B'   | Deactivate Data-Object-Font Component  | Object Container | :x:
DDOR         | X'D65C'   | Deactivate Data Object Resource        | Object Container | :x:
IDO          | X'D67C'   | Include Data Object                    | Object Container | :x:
RRR          | X'D65A'   | Remove Resident Resource               | Object Container | :x:
RRRL         | X'D659'   | Request Resident Resource List         | Object Container | :x:
WOCC         | X'D63C'   | Write Object Container Control         | Object Container | :x:
WOC          | X'D64C'   | Write Object Container                 | Object Container | :x:
BPS          | X'D65F'   | Begin Page Segment                     | Page Segment     | :x:
DPS          | X'D66F'   | Deactivate Page Segment                | Page Segment     | :x:
IPS          | X'D67F'   | Include Page Segment                   | Page Segment     | :x:
BO           | X'D6DF'   | Begin Overlay                          | Overlay          | :x:
DO           | X'D6EF'   | Deactivate Overlay                     | Overlay          | :x:
IO           | X'D67D'   | Include Overlay                        | Overlay          | :x:
LCP          | X'D61B'   | Load Code Page                         | Loaded Font      | :x:
LCPC         | X'D61A'   | Load Code Page Control                 | Loaded Font      | :x:
LF           | X'D62F'   | Load Font                              | Loaded Font      | :x:
LFCSC        | X'D619'   | Load Font Character Set Control        | Loaded Font      | :x:
LFC          | X'D61F'   | Load Font Control                      | Loaded Font      | :x:
LFI          | X'D60F'   | Load Font Index                        | Loaded Font      | :x:
LSS          | X'D61E'   | Load Symbol Set                        | Loaded Font      | :x:

# XOH Orders
The IPDS command "Execute Order Home State" (XOH) ist a rather complex command that isused to
execute orders when the printer is in *Home State*. The following table shows the orders and the
current status of the corresponding support of the order (*"supported"* means that ipdsbox can 
parse the order and create a specific Java object for it).

Code    | Order                          | Supported
--------|--------------------------------|----------
X'0200' | Deactivate Saved Page Group    | :white_check_mark:
X'0400' | Define Group Boundary          | :white_check_mark:
X'1300' | Eject to Front Facing          | :x:
X'0700' | Erase Residual Font Data       | :x:
X'0500' | Erase Residual Print Data      | :x:
X'F300' | Obtain Printer Characteristics | :x:
X'F500' | Page Counters Control          | :x:
X'0100' | Print Buffered Data            | :x:
X'0A00' | Remove Saved Page Group        | :x:
X'1500' | Select Input Media Source      | :x:
X'0E00' | Select Medium Modifications    | :x:
X'0900' | Separate Continuous Forms      | :x:
X'1600' | Set Media Origin               | :x:
X'1700' | Set Media Size                 | :x:
X'0300' | Specify Group Operation        | :x:
X'0D00' | Stack Received Pages           | :x:
X'F200' | Trace                          | :x:


# Triplets
The following table shows the Triplets and the current status of the corresponding support of the Triplet 
(*"supported"* means that ipdsbox can parse the Triplet and create a specific Java object for it).

ID    | Name                                          | Supported
------|-----------------------------------------------|-------------------
X'00' | Group ID                                      | :white_check_mark:
X'01' | Coded Graphic Character Set Global Identifier | :white_check_mark:
X'02' | Fully Qualified Name                          | :x:
X'4E' | Color Specification                           | :x:
X'50' | Encoding Scheme ID                            | :x:
X'5A' | Object Offset                                 | :x:
X'62' | Local Date and Time Stamp                     | :x:
X'6E' | Group Information                             | :white_check_mark:
X'70' | Presentation Space Reset Mixing               | :x:
X'74' | Toner Saver                                   | :x:
X'75' | Color Fidelity                                | :x:
X'79' | Metric Adjustment                             | :x:
X'84' | Font Resolution and Metric Technology         | :x:
X'85' | Finishing Operation                           | :white_check_mark:
X'86' | Text Fidelity                                 | :x:
X'88' | Finishing Fidelity                            | :x:
X'8B' | Data Object Font Descriptor                   | :x:
X'8D' | Linked Font                                   | :x:
X'8E' | UP3I Finishing Operation                      | :white_check_mark:
X'91' | Color Management Resource Descriptor          | :x:
X'92' | Invoke CMR                                    | :x:
X'95' | Rendering Intent                              | :x:

