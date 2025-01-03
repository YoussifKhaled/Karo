import { Box, Button, Stack, TextField, Typography } from "@mui/material";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import Password from "../../components/password/Password";

function Signup() {
  const navigate = useNavigate();
  
  const [name, setName] = useState("")
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState("");
  const [errorMessage, setErrorMessage] = useState("");
  const [passwordError, setPasswordError] = useState("");
  const [licensePlateNumber, setLicensePlateNumber] = useState("");
  const [licensePlateError, setLicensePlateError] = useState("");

  const fieldsSigned = () => {
    return (
      name.trim() && 
      email.trim() && 
      password.trim() && 
      confirmPassword.trim() && 
      licensePlateNumber.trim()
    );
  };

  const checkPasswordsMatch = () => {
    return password === confirmPassword;
  };

  const handleSignUp = async (e) => {
    e.preventDefault();

    if (!fieldsSigned()) {
      setErrorMessage('** Please fill out all fields');
      setPasswordError("")
      setLicensePlateError("")
      return;
    }

    if (!checkPasswordsMatch()) {
      setPasswordError('** Passwords do not match');
      setErrorMessage("")
      setLicensePlateError("")
      return;
    }

    if(password.length < 7){
      setPasswordError("The password must be at least 8 characters");
      setErrorMessage("")
      setLicensePlateError("")
      return;
    }
    if(password.length > 50){
      setPasswordError("The password must be at most 50 characters");
      setErrorMessage("")
      setLicensePlateError("")
      return;
    }

    setErrorMessage("");
    setPasswordError("");
    setLicensePlateError("")

    const registerUser = {
      name: name.trim(),
      email: email.trim(),
      password: password,
      licensePlateNumber: licensePlateNumber.trim(),
    };

    try {
      const response = await fetch(`http://localhost:8080/signup`,{
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify(registerUser)
      });
      if(response.status === 200){
        console.log('User successfully registered:');
        navigate('/');
      }
      else{
        // duplicate email message or duplicate license plate number
        const message = await response.text();
        if(message.includes("Email")) 
            setErrorMessage(message)
        else if(message.includes("License plate")) 
            setLicensePlateError(message)  
      }
       
    } catch (error) {
      console.log('Registration failed:', error.response ? error.response.data : error.message);
      setErrorMessage('Registration failed');
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
        width="400px"
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
          onSubmit={handleSignUp}
          noValidate
          width="350px"
          sx={{ mt: 1 }}
        >
          <Typography component="h1" variant="h5" fontWeight="700">
            Sign Up
          </Typography>
          <TextField
            margin="normal"
            required
            fullWidth
            id="name"
            label="Name"
            name="name"
            autoFocus
            value={name}
            onChange={(e) => setName(e.target.value)}
          />
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
          <Typography variant="subtitle2" color="red" height="20px">
            {errorMessage}
          </Typography>
          <Password
            value={password}
            setValue={setPassword}
            placeholder={"password"}
          />
          <Password
            value={confirmPassword}
            setValue={setConfirmPassword}
            placeholder={"confirm password"}
          />
          <Typography variant="subtitle2" color="red" height="20px">
            {passwordError}
          </Typography>
          <TextField
            margin="normal"
            required
            fullWidth
            id="licensePlateNumber"
            label="licensePlateNumber"
            name="licensePlateNumber"
            autoFocus
            value={licensePlateNumber}
            onChange={(e) => setLicensePlateNumber(e.target.value)}
          />
          <Typography variant="subtitle2" color="red" height="10px">
            {licensePlateError}
          </Typography>
          <Button
            type="submit"
            fullWidth
            variant="contained"
            sx={{ mt: 3, mb: 2 }}
          >
            Sign up
          </Button>
        </Stack>
      </Box>
    </Box>
  );
}

export default Signup;