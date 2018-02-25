# Javacard Embedded Test - Jianing Sun
## Instruction of the Android Application
1 Below is the main view of this application. From the requirements of this app, there are mainly
three parts (from top to bottom): 
* First part used to check and process user input command APDU - check command validity first, 
if it is valid, then process it for the next de/encryption and TLV parse.
* After getting the data stream of APDU, second part used to decrypt or encrypt the command. Decryption means 
cyclic left shift 2 bits, and encryption means cyclic right shift 2 bits.
* Thid part is the TLV parse part. It can parse the decrypted data and return with its tag-length-value information.
For our requirment, I also process different values into ASCII string and display it in the view.   

One thing need to pay attention to is that for a TLV stream, it probably embedded with several TLVs inside, 
for example, our given example for TLV parsing. (I'll show the result of that later)
<div align="center">
<!--   <img src="https://github.com/jianingsun21/javacard-apdu-tlv/blob/master/figures/1.jpeg" width="350" height=“400"> -->
  #### ![Figure 1](https://github.com/jianingsun21/javacard-apdu-tlv/blob/master/figures/1.jpeg)                                                                               
</div>
![Figure 1](https://github.com/jianingsun21/javacard-apdu-tlv/blob/master/figures/1.jpeg)

2 If user input an invalid command APDU, there would be a Toast showing that this is invalid. 
* If user input is invalid or no input, it would encrypt, decrypt and TLV parse the default given APDU strem:
80 E2 00 00 0A af 82 11 db db d9 08 12 9b d8. Otherwise it would process the input valid command APDU. 
* Below
<div align="center">
  <img src="https://github.com/jianingsun21/javacard-apdu-tlv/blob/master/figures/2.jpeg" width="350" height=“400">
</div>
