program 
  var N as int ; 
  var SQRT as int ; 
  var SQRT2 as int ;
begin 
  N := readint ; 
  SQRT := 0 ; 
  SQRT2 := 1 ;
  while SQRT * SQRT <= N do 
    SQRT := SQRT + 1 ; 
    SQRT2 := SQRT ;
  end ; 

  SQRT := SQRT - 1 ;

  writeint SQRT ; 

end 
