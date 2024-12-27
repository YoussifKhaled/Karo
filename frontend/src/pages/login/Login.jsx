import { Box, Button, Stack, TextField, Typography } from "@mui/material";
import React, { useState } from "react";
import {Link, useNavigate } from "react-router-dom";
import Password from "../../components/password/Password";
function Login() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [errorMessage, setErrorMessage] = useState("");

  const navigate = useNavigate();

  const handleLogin = async (e) => {
    setErrorMessage("");

    e.preventDefault();

    try{
      const response = await fetch(`http://localhost:8080/login?email=${email}&password=${password}`,{
          method: "POST",
          headers: {"Content-Type": "application/json"},
      });

      if(response.status === 200){
          const token = await response.text();
          localStorage.setItem("token", token);
          console.log("Login successful");
          navigate("/home");
      }
      else{
          const errorMessage = await response.text();
          console.log(errorMessage);
          setErrorMessage("Invalid username or password");
      }
      }catch(error){
          console.log("something went wrong");
      }
  };

  return (
    <Box bgcolor="#FBF6F6" height="100vh" 
      sx={{
      display: "flex",
      flexDirection: "column",
      alignItems: "center",
      justifyContent: "center"
      }}
    >
      <Box
        bgcolor="#DEDEDE"
        width="300px"
        p={3}
        borderRadius={2}
        sx={{
          display: "flex",
          flexDirection: "column",
          alignItems: "center",
          justifyContent: "center",
          boxShadow: 3,
        }}
      >
        <Stack
          component="form"
          onSubmit={handleLogin}
          noValidate
          sx={{ mt: 1 }}
        >
          <Typography component="h1" variant="h5" fontWeight="700">
            Login
          </Typography>
          <TextField
            margin="normal"
            required
            fullWidth
            id="email"
            label="Email"
            name="email"
            autoComplete="email"
            autoFocus
            value={email}
            onChange={(e) => setEmail(e.target.value)}
          />
          <Password
            value={password}
            setValue={setPassword}
            placeholder={"password"}
          />
          <Typography variant="subtitle2" color="red" height="10px">
            {errorMessage}
          </Typography>
          <Button
            type="submit"
            fullWidth
            variant="contained"
            sx={{ mt: 3, mb: 2 }}
          >
            Login
          </Button>
          <Typography variant="subtitle2" height="10px">
            Don't have account? <Link to="/signup">SignUp</Link>
          </Typography>
        </Stack>
      </Box>
    </Box>
  );
}

export default Login;
