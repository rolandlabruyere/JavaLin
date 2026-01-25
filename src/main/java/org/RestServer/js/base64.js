function codeBase64(string) 
{ 
  var out='', charCode=0, i=0, length=string.length; 
  var puffer=[];
  var base64EncodeChars ="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
  var dig1, dig2, dig3, dig4;

  var i = 0; 
  while(i < string.length) 
  { 
    charCode = string.charCodeAt(i++);
    if(charCode<0x80)  
    { 
      puffer[puffer.length]=charCode; 
    } 
    else if(charCode<0x800) 
    { 
      puffer[puffer.length]=0xc0|(charCode>>6); 
      puffer[puffer.length]=0x80|(charCode&0x3f); 
    }  
    else if(charCode<0x10000) 
    { 
      puffer[puffer.length]=0xe0|(charCode>>12); 
      puffer[puffer.length]=0x80|((charCode>>6)&0x3f); 
      puffer[puffer.length]=0x80|(charCode&0x3f); 
    }  
    else 
    { 
      puffer[puffer.length]=0xf0|(charCode>>18); 
      puffer[puffer.length]=0x80|((charCode>>12)&0x3f); 
      puffer[puffer.length]=0x80|((charCode>>6)&0x3f); 
      puffer[puffer.length]=0x80|(charCode&0x3f); 
    }  
    if(i==length) 
    { 
      while(puffer.length%3)  
      { 
        puffer[puffer.length]=NaN;
      } 
    } 
    if(puffer.length>2) 
    { 
      dig1 = puffer[0]>>2;
      dig2 = ((puffer[0]&3)<<4)|(puffer[1]>>4);
      dig3 = ((puffer[1]&15)<<2)|(puffer[2]>>6);
      dig4 = puffer[2]&63;

      if (isNaN(puffer[1])) {
        dig3 = dig4 = 64;
      } else if (isNaN(puffer[2])) {
        dig4 = 64;
      }

      puffer.shift();
      puffer.shift();
      puffer.shift();

      out+=base64EncodeChars.charAt(dig1); 
      out+=base64EncodeChars.charAt(dig2); 
      out+=base64EncodeChars.charAt(dig3); 
      out+=base64EncodeChars.charAt(dig4);
    } 
  } 
  return out; 
}
