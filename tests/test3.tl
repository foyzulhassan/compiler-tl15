program
   var SMALLER as int ;
   var BIGGER as int ;
   var TEMP as int ;
begin
   BIGGER := readint ;
   SMALLER := readint ;

   if SMALLER > BIGGER then
      TEMP := SMALLER ;
      SMALLER := BIGGER ;
      BIGGER := TEMP ;
   end ;

   while SMALLER > 0 do
      BIGGER := BIGGER - SMALLER ;

      if SMALLER > BIGGER then
         TEMP := SMALLER ;
         SMALLER := BIGGER ;
         BIGGER := TEMP ;
      end ;
   end ;
   writeint BIGGER ;
end