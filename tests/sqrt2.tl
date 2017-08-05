program 
  var N as int ; 
  var SQRT as int ; 
  var SQRTT as int ;
begin 
  N := readint ; 
  SQRT := 0 ; 
  SQRTT := 1 ;
  while SQRT * SQRT <= N do 
    SQRT := SQRT + 1 ; 
    SQRTT := SQRT ;
  end ; 

  SQRT := SQRT - 1 ;

  writeint SQRT ; 

end 