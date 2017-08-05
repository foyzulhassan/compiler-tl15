program 
   vaR N as int ; 
   var I as int ; 
begin 
   N := readint ; 

   I := 2 ; 
   if I > 3 then 
   while I <= N do 
      while N modI = 0 do 
         N := N div I ; 
         writeint I ; 
         if I > 2 then I := 5 ; end ;
      end ; 
      I := I + 4 ; 
   end ; 
   else  I := I + true ;
   end ;
   I := I mod 3 ;
     if I > 3 then  I := I + 4 ; 
      else  I := I + 3 ;
   end ;
end 