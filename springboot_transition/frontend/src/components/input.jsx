import { ScaleIcon } from '@heroicons/react/solid';
import axios from "axios";
import React, { useState } from "react";
import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';

const SignIn = () => {
    const url = "/api/student";
    const [student, setStudent] = useState({
      id: "",
      name: "",
      email: "",
      dob: ""
    });

    const handleChange = (e) => {
      const value = e.target.value;
      setStudent({...student, [e.target.name]: value});
    };
    const submitStudent = (e) => {
      e.preventDefault();
      e.stopPropagation();
      console.log(student);

      axios
      .post(url + "/addstudent", student)
      .then((res) => {
        console.log(res);
      })
      .catch((err) => {
        console.log(err);
      });
    }

  return (
    <>
      <div className="min-h-full flex align-middle items-center justify-center py-12 px-4 sm:px-6 lg:px-8" style={{height:"100vh"}}>
        <Container className="max-w-md w-full space-y-8 align-middle">
          <Row>
            <img
              className="mx-auto h-12 w-auto"
              src="https://tailwindui.com/img/logos/workflow-mark-indigo-600.svg"
              alt="Workflow"
            />
            <h2 className="mt-6 text-center text-3xl font-extrabold text-gray-900">PolitiPredict</h2>
            <p className="mt-2 text-center text-sm text-gray-600">
              <p href="#" className="font-medium text-indigo-600 hover:text-indigo-500">
                Gauge politicial bias of statements with AI
              </p>
            </p>
          </Row>
          <form id="studentform" className="mt-8 space-y-6" onSubmit = { e => submitStudent(e)}>
          <Row>
            <div className="rounded-md shadow-sm -space-y-px">
              <div>
                <label htmlFor="name" className="sr-only">
                  Email address
                </label>
                <input
                  id="NAME"
                  name="name"
                  type="text"
                  onChange = { e => handleChange(e)}
                  required
                  className="appearance-none rounded-none relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 rounded-t-md focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 focus:z-10 sm:text-sm"
                  placeholder="name"
                />
              </div>
            </div>
            <div className="rounded-md shadow-sm -space-y-px">
              <div>
                <label htmlFor="email" className="sr-only">
                  Email address
                </label>
                <input
                  id="EMAIL"
                  name="email"
                  type="email"
                  onChange = { (e) => handleChange(e)}
                  required
                  className="appearance-none rounded-none relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 rounded-t-md focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 focus:z-10 sm:text-sm"
                  placeholder="email"
                />
              </div>
            </div><div className="rounded-md shadow-sm -space-y-px">
              <div>
                <label htmlFor="dob" className="sr-only">
                  Email address
                </label>
                <input
                  id="DOB"
                  name="dob"
                  type="date"
                  onChange = {(e) => handleChange(e)}
                  required
                  className="appearance-none rounded-none relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 rounded-t-md focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 focus:z-10 sm:text-sm"
                  placeholder="date"
                />
              </div>
            </div>
            </Row>
            <Row>
            <div>
              <button
                type="submit"
                className="group relative w-full flex justify-center py-2 px-4 border border-transparent text-sm font-medium rounded-md text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
              >
                <span className="absolute left-0 inset-y-0 flex items-center pl-3">
                  <ScaleIcon className="h-5 w-5 text-indigo-500 group-hover:text-indigo-400" aria-hidden="true" />
                </span>
                Evaluate
              </button>
            </div>
            </Row>
          </form>
        </Container>
      </div>
    </>
  )
}


export default SignIn;