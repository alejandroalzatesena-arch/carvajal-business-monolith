// apiUrl vacio => los servicios piden rutas relativas (/api/...), que Nginx
// reenvia al backend (ver carvajal-ecommerce/nginx/default.conf.template).
// Frontend y API quedan en el mismo origen, asi que no interviene CORS.
// El build de desarrollo (`ng serve`) sigue usando environment.ts.
export const environment = {
  production: true,
  apiUrl: ''
};
