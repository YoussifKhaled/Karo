import { Visibility, VisibilityOff } from '@mui/icons-material';
import { IconButton, InputAdornment, TextField } from '@mui/material';
import React, { useState } from 'react'

function Password({value, setValue}) {
    const [showPassword, setShowPassword] = useState(false);

    const handleClickShowPassword = () => setShowPassword((show) => !show);
    return (
        <TextField
            margin="normal"
            required
            fullWidth
            name="password"
            label="Password"
            type={showPassword ? "text" : "password"}
            InputProps={{
              endAdornment: (
                <InputAdornment position="end">
                  <IconButton
                    aria-label={showPassword ? "hide the password" : "display the password"}
                    onClick={handleClickShowPassword}
                    edge="end"
                  >
                    {showPassword ? <VisibilityOff /> : <Visibility />}
                  </IconButton>
                </InputAdornment>
              ),
            }}
            autoComplete="current-password"
            value={value}
            onChange={(e) => setValue(e.target.value)}
          />
    )
}

export default Password;