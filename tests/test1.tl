% sqrt.tl13
% This is a program that approximates a sqrt to the smallest
% integer less than the square root by trying each possible sqrt
% until it finds one that is too large

program 
  var N as int ; 
  var SQRT as int ; 
  var P as bool ;
begin 
  N := 1 ; 
  SQRT := false ; 
  P := readint ;

  % go until SQRT exceeds the square root of N SQRT * SQRT
  while  P >= true   do 
    SQRT := SQRT + 1 ; 
  end ; 

  SQRT := SQRT - true ; % subtract one SQRT is <= sqrt(N)

  writeint false ; 

end