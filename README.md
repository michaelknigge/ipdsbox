# ipdsbox [![Build Status](https://travis-ci.org/michaelknigge/ipdsbox.svg?branch=master)](https://travis-ci.org/michaelknigge/ipdsbox) [![codecov.io](https://codecov.io/github/michaelknigge/ipdsbox/coverage.svg?branch=master)](https://codecov.io/github/michaelknigge/ipdsbox?branch=master) [![Coverity Status](https://scan.coverity.com/projects/8130/badge.svg)](https://scan.coverity.com/projects/8130)
Java library for parsing IPDS data streams. The library is currently in an early design phase and may not be useful for anything.

# IPDS Command Sets
The IPDS architecture contains several command sets. The following tables show the command sets and the
current status of the corresponding support of the IPDS command (*"supported"* means that ipdsbox can parse
the IPDS command and will create a specific Java Object for it).

## Device-Control Command Set
IPDS Command | Hex Value | Command Description            | Supported
------------ | --------- | -------------------------------|----------
AR           | X'D62E'  | Activate Resource               | :x: 
AFO          | X'D602'  | Apply Finishing Operations      | :x: 
BP           | X'D6AF'  | Begin Page                      | :x: 
DF           | X'D64F'  | Deactivate Font                 | :x: 
DUA          | X'D6CE'  | Define User Area                | :x: 
END          | X'D65D'  | End                             | :x: 
EP           | X'D6BF'  | End Page                        | :x: 
ISP          | X'D67E'  | Include Saved Page              | :x: 
ICMR         | X'D66B'  | Invoke CMR                      | :x: 
LCC          | X'D69F'  | Load Copy Control               | :x: 
LFE          | X'D63F'  | Load Font Equivalence           | :x: 
LPD          | X'D6CF'  | Logical Page Descriptor         | :x: 
LPP          | X'D66D'  | Logical Page Position           | :x: 
MID          | X'D601'  | Manage IPDS Dialog              | :x: 
NOP          | X'D603'  | No Operation                    | :x: 
PFC          | X'D634'  | Presentation Fidelity Control   | :x: 
RPO          | X'D67B'  | Rasterize Presentation Object   | :x: 
STM          | X'D6E4'  | Sense Type and Model            | :x: 
SHS          | X'D697'  | Set Home State                  | :x: 
SPE          | X'D608'  | Set Presentation Environment    | :x: 
XOA          | X'D633'  | Execute Order Anystate          | :x: 
XOH          | X'D68F'  | Execute Order Home State        | :x: 

## Text Command Set
IPDS Command | Hex Value | Command Description  | Supported
------------ | --------- | ---------------------|----------
LE           | X'D61D'   | Load Equivalence     | :x:
WTC          | X'D688'   | Write Text Control   | :x:
WT           | X'D62D'   | Write Text           | :x:

## IM-Image Command Set
IPDS Command | Hex Value | Command Description   | Supported
------------ | --------- | ----------------------|----------
WIC          | X'D63D'   | Write Image Control   | :x:
WI           | X'D64D'   | Write Image           | :x:

## IO-Image Command Set
IPDS Command | Hex Value | Command Description     | Supported
------------ | --------- | ------------------------|----------
WIC2         | X'D63E'   | Write Image Control 2   | :x:
WI2          | X'D64E'   | Write Image 2           | :x:

## Graphics Command Set
IPDS Command | Hex Value | Command Description     | Supported
------------ | --------- | ------------------------|----------
WGC          | X'D684'   | Write Graphics Control  | :x:
WG           | X'D685'   | Write Graphics          | :x:

## Bar Code Command Set
IPDS Command | Hex Value | Command Description      | Supported
------------ | --------- | -------------------------|----------
WBCC         | X'D680'   | Write Bar Code Control   | :x:
WBC          | X'D681'   | Write Bar Code           | :x:

## Object Container Command Set
IPDS Command | Hex Value | Command Description                    | Supported
------------ | --------- | ---------------------------------------|----------
DORE         | X'D66C'   | Data Object Resource Equivalence       | :x:
DDOFC        | X'D65B'   | Deactivate Data-Object-Font Component  | :x:
DDOR         | X'D65C'   | Deactivate Data Object Resource        | :x:
IDO          | X'D67C'   | Include Data Object                    | :x:
RRR          | X'D65A'   | Remove Resident Resource               | :x:
RRRL         | X'D659'   | Request Resident Resource List         | :x:
WOCC         | X'D63C'   | Write Object Container Control         | :x:
WOC          | X'D64C'   | Write Object Container                 | :x:

## Page Segment Command Set
IPDS Command | Hex Value | Command Description      | Supported
------------ | --------- | -------------------------|----------
BPS          | X'D65F'   | Begin Page Segment       | :x:
DPS          | X'D66F'   | Deactivate Page Segment  | :x:
IPS          | X'D67F'   | Include Page Segment     | :x:

## Overlay Command Set
IPDS Command | Hex Value | Command Description   | Supported
------------ | --------- | ----------------------|----------
BO           | X'D6DF'   | Begin Overlay         | :x:
DO           | X'D6EF'   | Deactivate Overlay    | :x:
IO           | X'D67D'   | Include Overlay       | :x:

## Loaded Font Command Set
IPDS Command | Hex Value | Command Description             | Supported
------------ | --------- | --------------------------------|----------
LCP          | X'D61B'   | Load Code Page                  | :x:
LCPC         | X'D61A'   | Load Code Page Control          | :x:
LF           | X'D62F'   | Load Font                       | :x:
LFCSC        | X'D619'   | Load Font Character Set Control | :x:
LFC          | X'D61F'   | Load Font Control               | :x:
LFI          | X'D60F'   | Load Font Index                 | :x:
LSS          | X'D61E'   | Load Symbol Set                 | :x:
