


result = do content <- readFile "13.data"
            let numbers = map (read:: String->Integer) (lines content)
            return $ take 10 $ show $ sum numbers