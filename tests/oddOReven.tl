% This program takes an Integer from the user
% It Returns 1 if the number is even
% It returns 0 if the number is odd
program
 var INPUT as int ;
 begin
  INPUT := readint ;
  if INPUT > 0 then
   if INPUT mod 2 = 0 then
    writeint 1 ;
   else
    writeint 0 ;
   end ;
  end ;
 end