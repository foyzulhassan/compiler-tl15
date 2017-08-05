program
    var X as int ;
    var Y as bool ;
    var Z as int ;

begin
    X := readint ;
	Z := 2 ;
	
    if X > 1 then
        X := Y div 2 ;
        X := X - 1 ;
    end ;

		Y := true ;
		while Z >= 2 do
        if X <= 1 then
            X := X * ( 1 + X )  ;
        else
            Y := 3 < 5 ;
        end ;
    end ;

    writeint Y ;

end  